import { View, StyleSheet } from "react-native";

type Props = {};
export default function MultipleStyles({}: Props) {
  return (
    <View>
      {/*applying multiple styles*/}
      <View style={[styles.box, styles.lightblueBg]}></View>
      <View style={[styles.box, styles.lightGreenBg]}></View>
    </View>
  );
}

const styles = StyleSheet.create({
  box: {
    width: 100,
    height: 100,
    padding: 10,
  },
  lightblueBg: {
    backgroundColor: "lightblue",
  },
  lightGreenBg: {
    backgroundColor: "lightgreen",
  },
});