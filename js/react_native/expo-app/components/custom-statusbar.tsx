import { StatusBar, View } from "react-native";

type Props = {};
export default function CustomStatusbar({}: Props) {
  return (
    <View>
      <StatusBar
        backgroundColor={"lightgreen"} //color of the status bar
        barStyle={"light-content"} //icons colors
        hidden //to hide the status bar
      />
    </View>
  );
}