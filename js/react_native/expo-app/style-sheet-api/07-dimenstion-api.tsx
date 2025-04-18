import { StyleSheet, Text, View, Dimensions } from "react-native";

type Props = {};
const Dimension = (props: Props) => {
  return (
    <View>
      <Text>Dimension</Text>
    </View>
  );
};

const windowWidth = Dimensions.get("window").width;
const height = Dimensions.get("window").height;

export default Dimension;
const styles = StyleSheet.create({
  box: {
    width: windowWidth > 500 ? "70%" : "89%",
  },
});

/*
Dimension Api draw back is it doesn't accommodate when user switches to landscape
 */