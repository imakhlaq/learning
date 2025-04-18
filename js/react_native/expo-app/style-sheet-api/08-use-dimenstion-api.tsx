import { useWindowDimensions, StyleSheet, View } from "react-native";

type Props = {};
const DimensionApiHook = (props: Props) => {
  const windowWidth = useWindowDimensions().width;
  const windowHeight = useWindowDimensions().height;

  return (
    <View
      style={[
        style.box,
        {
          width: windowWidth > 900 ? 200 : 300,
        },
      ]}
    >
      DimensionApiHook
    </View>
  );
};
export default DimensionApiHook;

const style = StyleSheet.create({
  box: {
    width: 300,
    height: 400,
  },
});