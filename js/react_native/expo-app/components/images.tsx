import { Image, View, ImageBackground, Text } from "react-native";

// @ts-ignore
import logo from "../assets/images/adaptive-icon.png";

type Props = {};
export default function Images({}: Props) {
  return (
    <View>
      <Image source={logo} style={{ width: 300, height: 300 }} />

      <Image
        source={{ uri: "https://picsum.photos/300" }}
        style={{ width: 300, height: 300 }}
      />

      <ImageBackground source={{ uri: "https://picsum.photos/400" }}>
        <Text>
          LSots Lorem ipsum dolor sit amet, consectetur adipisicing elit. A
          alias, beatae culpa cumque eligendi enim itaque magni, minus,
          molestias quia quisquam quod repellendus voluptate? Asperiores
          consequatur deserunt natus voluptatem voluptatum!
        </Text>
      </ImageBackground>
    </View>
  );
}