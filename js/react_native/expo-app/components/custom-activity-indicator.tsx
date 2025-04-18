import { ActivityIndicator, View } from "react-native";

type Props = {};
export default function CustomActivityIndicator({}: Props) {
  return (
    <View style={{ flex: 1, backgroundColor: "plum", padding: 60 }}>
      <ActivityIndicator size={"large"} color={"green"} animating={true} />
    </View>
  );
}