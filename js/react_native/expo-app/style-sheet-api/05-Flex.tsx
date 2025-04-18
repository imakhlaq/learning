import { StyleSheet, Text, View } from "react-native";
import React from "react";

const FlexContainer = () => {
  return (
    <View style={stylesContainer.container}>
      <FlexItems>Box 1</FlexItems>
      <FlexItems>Box 2</FlexItems>
      <FlexItems>Box 3</FlexItems>
      <FlexItems>Box 4</FlexItems>
      <FlexItems>Box 5</FlexItems>
      <FlexItems>Box 6</FlexItems>
      <FlexItems>Box 7</FlexItems>
    </View>
  );
};
export default FlexContainer;
const stylesContainer = StyleSheet.create({
  container: {
    marginTop: 64,
    borderWidth: 6,
    borderColor: "red",
    borderStyle: "dotted",
  },
});

type Props = {
  children: React.ReactNode;
};
const FlexItems = ({ children }: Props) => {
  return (
    <View style={stylesItems.box}>
      <Text style={stylesItems.text}>{children}</Text>
    </View>
  );
};

const stylesItems = StyleSheet.create({
  box: {
    flex: 2,
    backgroundColor: "#fff",
    padding: 20,
  },
  text: {
    fontSize: 24,
    fontWeight: "bold",
    textAlign: "center",
  },
});

/*
Flex
 -> In react native View is already have display flex.

 flex:1 (on container)
     -> it means container will occupy all the screen size

flex:1, 2, 3, 4 (item)
    -> If you set flex property on the flex item then it specifies how much space that item

flex direction: row, reverse, column, row-column
    -> change the direction of flex

justifyContent:  flex-start, flex-center, flex-end, flex-between, flex-around etc.
    -> applied on the Flex container to control alignment of flex item in x-axis.(in column)
    ->

alignItems: stretch, flex-center, flex-start, flex-end, flex-baseline.
    -> applied on the Flex container to control alignment of flex item y-axi.( in column)

    -> stretch the item and take all the available space in the cross access.

    -> flex-start pushes the item to the start of the cross access

    -> flex-baseline align the items base on the content

alignSelf:  flex-start, flex-center, flex-end, flex-stretch
    -> use on the flexItem to control its placement of a flex Item in the flex container

    default value for alignSelf is auto which means it inherit the alignItems property

flexWrap: noWrap, wrap, wrap-reverse
    -> how flex items behave when there is limited space in container
    -> noWrap is the default value if space is not enough item will overflow


alignContent:  flex-start, flex-center, flex-end, flex-between, flex-around etc.
    -> it ony works when we have multiple rows in the flex container (by setting flex wrap)
    -> it align all the items (multiple rows/ columns) of a flex column in cross axis.

flexGap: rowGap, columGap, gap

flexBasis: 300
    -> it set the initial size of the flex item its alternative to use height and width
        but don't use height and width because distribution of space when you do flex1 on specific iteam
        then the distribution is not good

flexShrink: 1,4,6
        -> when the layout shrink than a specific item should shrink more and less compare to other items

        the default value for the flexShrink is 0 which means if the container size is small then the items will overflow the container.

        Note if the item size is overflowing the flex container then set flex shrink to 1 or...

        flex shrink is relative to other items flexShrink 2 will shrink twice more than other items

flexGrow: 1,4,6
        -> flexGrow determines how much an item should occupy when there is extra space is available in the flex container

          flex grow is relative to other items in the container just like flexShrink

          if you set flexShrink:1 to the flex item it will take all available space in the flexContainer and if you set another flexItem
          to 1 then the available space between the two items

        


 
 */