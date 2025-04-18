import { View, Text } from "react-native";

type Props = {};
export default function InlineStyle({}: Props) {
  return (
    // This is an inline style
    <View style={{ flex: 1, backgroundColor: "yellow", padding: 23 }}>
      <Text>Style Sheet</Text>
    </View>
  );
}