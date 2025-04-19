import { StyleSheet, Text, TextInput, View } from "react-native";
import { useState } from "react";

type Props = {};
const CustomInp = (props: Props) => {
  const [name, setName] = useState("");

  return (
    <View>
      <TextInput
        value={name}
        onChangeText={(e) => setName(e)}
        placeholder={"example@gmial.com"}
        secureTextEntry={true} //make input hidden
        autoCapitalize={"none"} //don't capitalize the words
        autoComplete={"name"} //
        autoCorrect={false} //disable auto correct
        keyboardType={"numeric"} //  type of keyboard when user types
      />
      {/* Text area*/}
      <TextInput
        placeholder={"message"}
        multiline={true}
        style={{ height: 300, textAlignVertical: "top" }}
      />
    </View>
  );
};
export default CustomInp;
const styles = StyleSheet.create({});