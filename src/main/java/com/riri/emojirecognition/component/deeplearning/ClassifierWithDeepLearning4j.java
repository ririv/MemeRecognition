package com.riri.emojirecognition.component.deeplearning;

import com.riri.emojirecognition.domain.Model;
import com.riri.emojirecognition.service.ModelService;
import javafx.util.Pair;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
//@PropertySource(value = "classpath:application.properties",encoding = "utf-8")
//@RefreshScope
public class ClassifierWithDeepLearning4j {

    private static MultiLayerNetwork model;

    private static NativeImageLoader loader;

    private static DataNormalization scaler;

    public static class Model{

        private static String path;

        private static int height;

        private static int width;

        private static int channels; //通道

        private static String[] labels;

        public static String getPath() {
            return path;
        }

        public static void setPath(String path) {
            Model.path = path;
        }

        public static int getHeight() {
            return height;
        }

        public static void setHeight(int height) {
            Model.height = height;
        }

        public static int getWidth() {
            return width;
        }

        public static void setWidth(int width) {
            Model.width = width;
        }

        public static int getChannels() {
            return channels;
        }

        public static void setChannels(int channels) {
            Model.channels = channels;
        }

        public static String[] getLabels() {
            return labels;
        }

        public static void setLabels(String[] labels) {
            Model.labels = labels;
        }
    }

//    @Value("${classifier.modelResourcePath}")
//    private String modelPath;
//    @Value("${classifier.height}")
//    private int height;
//    @Value("${classifier.width}")
//    private int width;
//    @Value("${classifier.channels}")
//    private int channels; //通道
//    @Value("${classifier.labels}")
//    private String[] labels;

    // 将初始化工作设为静态方法，在启动程序时就调用并初始化
    // 这样可以防止每次预测时都会new一个新的模型实例，重复加载相同模型等其他相关配置，大大提升预测效率
    public static void init() throws IOException,InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {
        // ClassPath为resources目录，这里相对于此路径
        final String fullModel = new ClassPathResource(Model.path).getFile().getPath();

        // Keras Sequential models correspond to DL4J MultiLayerNetworks. We enforce loading the training configuration
        // of the model as well. If you're only interested in inference, you can safely set this to 'false'.
        // 如果模型仅用来测试，不用作训练，参数设置为false
        model = KerasModelImport.importKerasSequentialModelAndWeights(fullModel, false);

        // 使用NativeImageLoader转换为数值矩阵
        loader = new NativeImageLoader(Model.height, Model.width, Model.channels); // 加载和缩放

        scaler = new ImagePreProcessingScaler();//预处理 默认缩放至 0-1
    }

    public INDArray predict(File image) throws IOException{

//        NativeImageLoader loader = new NativeImageLoader(Model.height, Model.width, Model.channels);
        INDArray imageINDArray = loader.asMatrix(image); // 创建INDarray
//        System.out.println(Arrays.toString(imageINDArray.shape()));//shape为[1, 3, 96, 96]，数量，深度，高度，宽度

//        DataNormalization scaler = new ImagePreProcessingScaler();
        scaler.transform(imageINDArray);

//        INDArray output = model.output(imageINDArray);   // 获得模型对图像的预测，得到的是概率值
//        System.out.println(output.toString());
//        System.out.println(output.argMax(1));

        return model.output(imageINDArray);
    }

    public INDArray predict(String imagePath) throws IOException, InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {
        File file = new File(imagePath);
        return predict(file); //看清参数类型，否则则变成递归
    }

    //pair是一个元组，用来传递两个类型不同的值
    public Pair<String,Float> classify(File image) throws IOException, InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {
        INDArray output = predict(image);
//        System.out.println(output.argMax());
        int index = output.argMax(1).getInt(0);
//        System.out.println(Arrays.toString(labels)); //打印标签
        if (Model.labels != null) {
//            String predictLabel = labels[index];
//            System.out.println(predictLabel);
//            System.out.println(output.getFloat(index));//打印概率
            return new Pair<>(Model.labels[index],output.getFloat(index));
        } else {
            return null;
        }
    }

    public Pair<String,Float> classify(String imagePath) throws IOException, InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {
        File file = new File(imagePath);
        return classify(file);//看清参数类型，否则则变成递归
    }

}