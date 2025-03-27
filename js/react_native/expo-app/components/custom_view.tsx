import { Text, View } from "react-native";

//View is essential is a div ( not scrollable)
// it is a container for other components
type Props = {};
export default function CustomView({}: Props) {
  return (
    <View style={{ flex: 1, backgroundColor: "plum" }}>
      <View style={{ width: 300, height: 200, backgroundColor: "red" }}>
        <Text>HElloLL</Text>
      </View>

      <View
        style={{
          width: 300,
          height: 400,
          backgroundColor: "yellow",
          borderColor: "black",
        }}
      >
        <Text>HElloL3</Text>
      </View>
    </View>
  );
}