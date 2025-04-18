import { ImageBackground, ScrollView, Text, View } from "react-native";
import CustomButton from "@/components/custom_button";
import CustomActivityIndicator from "@/components/custom-activity-indicator";
import CustomAlert from "@/components/custom-alert";

type Props = {};
export default function CustomScrollView({}: Props) {
  return (
    <View style={{ padding: 10 }}>
      <ScrollView style={{ borderColor: "red" }}>
        <ImageBackground
          source={{ uri: "https://picsum.photos/400" }}
          style={{ width: 400, height: 600 }}
        >
          <Text>
            LSots Lorem ipsum dolor sit amet, consectetur adipisicing elit. A
            alias, beatae culpa cumque eligendi enim itaque magni, minus,
            molestias quia quisquam quod repellendus voluptate? Asperiores
            consequatur deserunt natus voluptatem voluptatum!
          </Text>
        </ImageBackground>
        <Text>
          Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ab neque
          nostrum perspiciatis veritatis! Accusantium aperiam asperiores beatae,
          commodi corporis cumque dicta dolorem ducimus eos esse est eveniet
          exercitationem explicabo fugit inventore ipsa ipsum laboriosam
          molestiae numquam obcaecati perspiciatis possimus qui quis reiciendis
          rem similique sit, sunt suscipit tempora tempore tenetur totam ullam
          ut veritatis vero voluptates voluptatibus! Assumenda dolore eos et
          excepturi. Alias assumenda culpa distinctio dolor dolore ea eum
          explicabo fugit ipsum nam natus nesciunt odio officia officiis placeat
          porro quidem quisquam recusandae repellat reprehenderit sed soluta
          sunt suscipit tempore unde veniam vero, voluptatum! Ab accusamus
          adipisci alias aspernatur culpa esse impedit magnam molestiae natus
          neque numquam odio officia, quaerat quo ratione reprehenderit unde ut
          voluptatum. Architecto debitis distinctio enim excepturi incidunt
          laborum laudantium sequi soluta totam veritatis, vero voluptate,
          voluptatum. Accusantium dolor ea est facere fuga illo quasi ratione
          sunt temporibus vitae! Alias asperiores delectus dolor dolore dolorem,
          doloremque eaque enim et explicabo id illo in ipsa iure libero magni
          nesciunt nulla porro possimus praesentium ratione reiciendis rem
          repudiandae saepe unde ut vel voluptas. A, autem corporis dignissimos
          doloremque dolores est exercitationem incidunt, minima nemo optio quo
          tenetur vel voluptate. Ab amet cupiditate dolor dolore eligendi fugit
          porro possimus sed temporibus voluptas? Aperiam consequuntur impedit,
          inventore mollitia nobis odit voluptatem voluptatum? Aliquam earum
          iusto minus optio quasi quibusdam sint? Adipisci alias aperiam
          blanditiis cumque dicta dolore ducimus eaque earum eos esse eum
          expedita fugit impedit ipsum iure iusto labore laudantium libero
          maxime necessitatibus odit omnis recusandae rem repudiandae, tempore
          unde veritatis voluptates. Ab adipisci architecto autem consequuntur
          corporis culpa cumque dicta distinctio doloribus eligendi, error
          exercitationem facilis fugit hic ipsam laborum nemo nihil nisi non
          obcaecati placeat quae quis quisquam reiciendis sit, ut voluptatum!
          Asperiores dolorum ex inventore iure natus numquam quae quod repellat.
          Ab dolore illum, nemo nulla officiis provident quasi quo recusandae
          sequi similique sint suscipit ut veniam! Ab, amet animi asperiores
          consequatur debitis deserunt eaque earum facilis harum ipsum iure
          labore maxime nostrum odit, quaerat ratione rem saepe suscipit velit
          veniam veritatis vitae voluptates!
        </Text>
        <CustomButton />

        <CustomActivityIndicator />
        <CustomAlert />
      </ScrollView>
    </View>
  );
}