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
import org.nd4j.linalg.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
//@PropertySource(value = "classpath:application.properties",encoding = "utf-8")
public class ClassifierWithDeepLearning4j {

    @Value("${classifier.modelResourcePath}")
    private String modelResourcePath;
    @Value("${classifier.height}")
    private int height;
    @Value("${classifier.width}")
    private int width;
    @Value("${classifier.channels}")
    private int channels; //通道
    @Value("${classifier.labels}")
    private String[] labels;


    public INDArray predict(File image) throws IOException, InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {

        // ClassPath为resources目录，这里相对于此路径
        final String fullModel = new ClassPathResource(modelResourcePath).getFile().getPath();

        // Keras Sequential models correspond to DL4J MultiLayerNetworks. We enforce loading the training configuration
        // of the model as well. If you're only interested in inference, you can safely set this to 'false'.
        // 如果模型仅用来测试，不用作训练，参数设置为false
        MultiLayerNetwork model = KerasModelImport.importKerasSequentialModelAndWeights(fullModel, false);

        // 使用NativeImageLoader转换为数值矩阵
        NativeImageLoader loader = new NativeImageLoader(height, width, channels); // 加载和缩放
        INDArray imageINDArray = loader.asMatrix(image); // 创建INDarray
//        System.out.println(Arrays.toString(imageINDArray.shape()));//shape为[1, 3, 96, 96]，数量，深度，高度，宽度
        DataNormalization scaler = new ImagePreProcessingScaler();//预处理 默认缩放至 0-1
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
        if (labels != null) {
//            String predictLabel = labels[index];
//            System.out.println(predictLabel);
//            System.out.println(output.getFloat(index));//打印概率
            return new Pair<>(labels[index],output.getFloat(index));
        } else {
            return null;
        }
    }

    public Pair<String,Float> classify(String imagePath) throws IOException, InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {
        File file = new File(imagePath);
        return classify(file);//看清参数类型，否则则变成递归
    }

}