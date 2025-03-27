import { Text, View } from "react-native";

type Props = {};
export default function CustomText({}: Props) {
  return (
    <View>
      <Text>
        <Text style={{ color: "pink" }}>
          Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium
          animi aperiam commodi,
        </Text>{" "}
        dicta dolore eaque eligendi in inventore natus non perspiciatis possimus
        quae quaerat quasi quos ratione rem sed suscipit unde veniam!
      </Text>
    </View>
  );
}