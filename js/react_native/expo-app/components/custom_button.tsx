import { Button, View } from "react-native";

type Props = {};
export default function CustomButton({}: Props) {
  return (
    <View>
      <Button
        title={"PRESS ME"}
        disabled={false}
        color={"red"}
        onPress={(event) => console.log("BUTTON PRESSED")}
      />
    </View>
  );
}