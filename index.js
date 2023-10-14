import { NativeModules } from "react-native";

const { BlurImageModule } = NativeModules;

export const getBlurredImage = (url,x,y,width,height,callback) => {
  BlurImageModule.blurImage(url,x,y,width,height,callback);
};
