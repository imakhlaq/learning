import { Alert, Button, View } from "react-native";

/*
Just like alert there are many apis in react native
 */

type Props = {};
export default function CustomAlert({}: Props) {
  return (
    <View>
      <Button
        title={"Alert"}
        onPress={() =>
          Alert.alert("Invalid Data!", "DOB is incorrect", [
            {
              text: "OK",
              onPress: () => console.log("OK CLICKED"),
            },
            {
              text: "Cancel",
              onPress: () => console.log("Cancel CLICKED"),
            },
          ])
        }
      />
    </View>
  );
}