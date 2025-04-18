import { StyleSheet, Text, View, SafeAreaView } from "react-native";

type Props = {};
const safe = (props: Props) => {
  return (
    <SafeAreaView style={styles.safeArea}>
      <View>
        <Text>safe</Text>
      </View>
    </SafeAreaView>
  );
};
export default safe;
const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    borderColor: "transparent",
  },
});

/*
SafeAreaView is a component use to safely render UI in the safer area. ( excluding notch etc. )

 */