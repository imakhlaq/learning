import { StyleSheet, Text, View } from "react-native";

type Props = {};
const RelativeLayout = (props: Props) => {
  return (
    <View>
      <Text>RelativeLayout</Text>
    </View>
  );
};
export default RelativeLayout;
const styles = StyleSheet.create({});

/*
Relative layout
    -> element is position according to the normal flow of the layout.
    -> but it can be offset using from its position using top,bottom and left right values.
    -> off set doesn't affect the position of the sibling elements

absolute layout
    -> element doesn't take part in the normal flow of the layout.
    -> position of element is determined by the top,bottom and left right values.
    -> its position is determined by the parent container
    -> its laid out independent of its siblings
 */