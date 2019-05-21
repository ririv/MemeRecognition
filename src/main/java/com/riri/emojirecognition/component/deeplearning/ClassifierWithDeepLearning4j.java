package com.riri.emojirecognition.component.deeplearning;

import javafx.util.Pair;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;

@Component
public class ClassifierWithDeepLearning4j {

    //flag为0是使用系统应用的模型，为1是使用用户所选择的临时使用的另外的模型

    private MultiLayerNetwork enabledModel;//被应用的主要模型，记录至数据库

    private MultiLayerNetwork selectedModel;//被选择的备用模型，临时使用，不记录至数据库

    private NativeImageLoader enabledModelLoader;

    private NativeImageLoader selectedModelLoader;

    private DataNormalization scaler;

    private Long enabledModelId; //记录模型id

    private String[] enabledModelLabels;

    private Long selectedModelId; //记录模型id

    private String[] selectedModelLabels;

    public String[] getEnabledModelLabels() {
        return enabledModelLabels;
    }

    public Long getEnabledModelId() {
        return enabledModelId;
    }

    public String[] getSelectedModelLabels() {
        return selectedModelLabels;
    }

    public Long getSelectedModelId() {
        return selectedModelId;
    }

    public class Model {

        private Long id;

        @NotNull
        private String path;

        private int height;

        private int width;

        private int channels; //通道

        @NotNull
        private String[] labels;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getChannels() {
            return channels;
        }

        public void setChannels(int channels) {
            this.channels = channels;
        }

        public String[] getLabels() {
            return labels;
        }

        public void setLabels(String[] labels) {
            this.labels = labels;
        }

        public void setLabels(String labels) {
            this.labels = labels.split(",");
        }
    }

    // 将初始化工作设为init()，在启动程序时就调用并初始化
    // 这样可以防止每次预测时都会new一个新的模型实例，重复加载相同模型等其他相关配置，大大提升预测效率
    // 需要重新载入新模型配置时也可以调用
    public void init(Model model, int flag) throws IOException, InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {

        // ClassPath为resources目录，这里相对于此路径
//        final String fullModel = new ClassPathResource(Model.path).getFile().getPath();

        //使用非ClassPath的外部路径
        System.out.println("Model path is " + model.path);

        // Keras Sequential models correspond to DL4J MultiLayerNetworks. We enforce loading the training configuration
        // of the model as well. If you're only interested in inference, you can safely set this to 'false'.
        // 如果模型仅用来测试，不用作训练，参数设置为false
        if (flag == 0) {
            enabledModel = KerasModelImport.importKerasSequentialModelAndWeights(model.path, false);

            // 使用NativeImageLoader转换为数值矩阵
            enabledModelLoader = new NativeImageLoader(model.height, model.width, model.channels); // 加载和缩放

            enabledModelLabels = model.labels;

            enabledModelId = model.id;

        } else {
            selectedModel = KerasModelImport.importKerasSequentialModelAndWeights(model.path, false);

            // 使用NativeImageLoader转换为数值矩阵
            selectedModelLoader = new NativeImageLoader(model.height, model.width, model.channels); // 加载和缩放

            selectedModelLabels = model.labels;

            selectedModelId = model.id;
        }

        scaler = new ImagePreProcessingScaler();//预处理 默认缩放至 0-1
    }


    //仅输出预测结果
    public INDArray predict(File image, int flag) throws IOException {
        INDArray imageINDArray;
        if (flag == 0) {
            imageINDArray = enabledModelLoader.asMatrix(image); // 创建INDarray
//        System.out.println(Arrays.toString(imageINDArray.shape()));//shape为[1, 3, 96, 96]，数量，深度，高度，宽度
        } else {
            imageINDArray = selectedModelLoader.asMatrix(image);
        }

        scaler.transform(imageINDArray);

//        INDArray output = model.output(imageINDArray);   // 输出模型对图像的预测，得到的是概率值
//        System.out.println(output.toString());
//        System.out.println(output.argMax(1));
        if (flag == 0) {
            return enabledModel.output(imageINDArray);
        } else {
            return selectedModel.output(imageINDArray);
        }
    }

    public INDArray predict(String imagePath, int flag) throws IOException {
        File file = new File(imagePath);
        return predict(file, flag); //看清参数类型，否则则变成递归
    }

    //输出具体的标签和概率
    //pair是一个元组，用来传递两个类型不同的值
    public Pair<String, Float> classify(File image, int flag) throws IOException {
        String[] labels;

        if (flag == 0) {
            labels = enabledModelLabels;
        } else {
            labels = selectedModelLabels;
        }

        INDArray output = predict(image, flag);
//        System.out.println(output.argMax());
        int index = output.argMax(1).getInt(0);
//        System.out.println(Arrays.toString(labels)); //打印标签
        if (labels != null) {
//            String predictLabel = labels[index];
//            System.out.println(predictLabel);
//            System.out.println(output.getFloat(index));//打印概率
            return new Pair<>(labels[index], output.getFloat(index));
        } else {
            return null;
        }
    }

    public Pair<String, Float> classify(String imagePath, int flag) throws IOException {
        File file = new File(imagePath);
        return classify(file, flag);//看清函数参数类型，否则则变成递归
    }

}