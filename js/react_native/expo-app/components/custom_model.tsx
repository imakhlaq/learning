import { Button, Modal, Text, View } from "react-native";
import { useState } from "react";

//by default model is always active

type Props = {};
export default function CustomModel({}: Props) {
  const [isOpen, setIsOpen] = useState(true);

  return (
    <Modal
      visible={isOpen}
      animationType={"slide"}
      presentationStyle={"pageSheet"}
    >
      <View>
        <Text>
          Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ab deleniti
          fugiat itaque laborum magni provident ratione soluta. Amet aperiam
          cupiditate deserunt, dignissimos facere natus obcaecati possimus quas
          quo, reiciendis repellendus.
        </Text>
        <Button
          title={"close"}
          color={"red"}
          onPress={() => setIsOpen((prevState) => !prevState)}
        />
      </View>
    </Modal>
  );
}