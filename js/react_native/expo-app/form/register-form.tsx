import {
  Button,
  KeyboardAvoidingView,
  Platform,
  StyleSheet,
  Text,
  TextInput,
  View,
} from "react-native";
import { useState } from "react";

type Props = {};
const RegisterForm = (props: Props) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  function handleSubmit() {
    //validate
    //reset the input
    setPassword("");
    setUsername("");
  }

  return (
    //when keyboard is visible KeyboardAvoidingView pushes the wrapped elements above the keyboard
    //behavior={"padding"} add padding to push the element
    //instead of hiding it under the keyword
    <KeyboardAvoidingView
      behavior={"padding"}
      keyboardVerticalOffset={Platform.OS == "ios" ? 100 : 0} //fixes the offset on the keyboard
      style={styles.container}
    >
      <View style={styles.form}>
        <Text style={styles.label}>Username</Text>
        <TextInput
          style={styles.input}
          placeholder={"username"}
          value={username}
          onChangeText={setUsername}
        />

        <Text style={styles.label}>Password</Text>
        <TextInput
          style={styles.input}
          placeholder={"password"}
          secureTextEntry={true}
          value={password}
          onChangeText={setPassword}
        />
        <Button title={"Login"} onPress={handleSubmit} />
      </View>
    </KeyboardAvoidingView>
  );
};
export default RegisterForm;
const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    paddingHorizontal: 20,
    backgroundColor: "#f5f5f5",
  },
  form: {
    backgroundColor: "white",
    padding: 20,
    borderRadius: "10",
    shadowColor: "black",
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 4,
    elevation: 5, //shadow for android
  },
  label: {
    fontSize: 16,
    marginBottom: 5,
    fontWeight: "bold",
  },
  input: {
    height: 40,
    borderColor: "#ddd",
    borderWidth: 1,
    marginBottom: 15,
    padding: 10,
    borderRadius: 5,
  },
});