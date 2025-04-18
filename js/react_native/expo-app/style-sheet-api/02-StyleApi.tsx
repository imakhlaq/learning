import { StyleSheet, Text, View } from "react-native";

type Props = {};
export default function StyleApi({}: Props) {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>StyleSheet API</Text>
    </View>
  );
}
const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: "plum", padding: 24 },
  title: {},
});