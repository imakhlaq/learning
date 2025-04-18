import { Text, View } from "react-native";
import CustomView from "@/components/custom_view";
import CustomText from "@/components/custom_text";
import Images from "@/components/images";
import CustomScrollView from "@/components/custom_scroll_view";
import CustomModel from "@/components/custom_model";
import CustomStatusbar from "@/components/custom-statusbar";
import CustomActivityIndicator from "@/components/custom-activity-indicator";

export default function Index() {
  return (
    <View>
      {/*<CustomView/>*/}
      {/*<CustomText/>*/}
      {/*<Images/>*/}
      <CustomScrollView />
      <CustomModel />
      <CustomStatusbar />
    </View>
  );
}