import { Platform, StyleSheet, Text, View } from "react-native";

type Props = {};
const PlateFormS = (props: Props) => {
  if (Platform.OS === "windows") {
    //platform Specific code
  }

  return (
    <View style={styles.test1}>
      <Text>PlateFormS</Text>
    </View>
  );
};
export default PlateFormS;
const styles = StyleSheet.create({
  test1: {
    width: Platform.OS === "android" ? 23 : 0,
  },

  text: {
    ...Platform.select({
      ios: {}, //ios styles
      android: {}, // android specific styles
    }),
  },
});

/*
For even more complex components
you can create components with different palteform with its platefrom extension
and react native will use that component
eg
  custom-button.ios.tsx
  custom-button.android.tsx


  to import
  import CustomButton from "../components/custom-button"


  <CustomButton/>    // react native will import platform specific component
 */