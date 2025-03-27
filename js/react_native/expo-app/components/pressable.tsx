/*
Pressable are used to wrap the elements on which you want to add onPress etc

 onPress
 onPressIn is called when a press is active
 onLongPress is triggered when a press is held for longer tha 500ms
 onPressOut is called when a press is deactivated
 */

import { View, Pressable, Text } from "react-native";

type Props = {};
export default function CustomPressable({}: Props) {
  return (
    <View>
      <Pressable onPress={() => {}} onLongPress={() => {}}>
        <View>
          <Text>
            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ab aperiam
            beatae dolorem eos explicabo, non perferendis quas, quasi sequi
            veniam vitae voluptates! Adipisci amet asperiores aut cumque earum
            eius explicabo ipsa ipsum libero magnam mollitia nihil optio
            perspiciatis placeat praesentium qui sapiente, temporibus totam
            ullam, voluptatibus. Ad, amet assumenda at doloribus earum harum
            itaque quae? Debitis dolores ducimus eaque expedita libero natus
            officiis quas sit tempora voluptate. Aliquid aperiam architecto
            assumenda corporis debitis deleniti dicta eligendi error et eveniet
            harum, hic iste labore laudantium, maxime minus modi necessitatibus
            nemo nihil nisi nobis, numquam obcaecati odio perferendis
            perspiciatis placeat porro quos reiciendis rerum sunt unde veritatis
            vitae voluptatem! Aperiam consectetur doloribus earum inventore
            itaque minima quidem quos! Accusantium animi provident quasi sunt
            unde. Ab, aliquid amet consectetur cumque deleniti dignissimos
            dolore doloremque dolorum id incidunt ipsum libero, minus mollitia
            natus, odio officia perferendis quaerat quasi reprehenderit sapiente
            sequi sit voluptatibus? Adipisci, aperiam architecto delectus
            deleniti, eum laborum minima nihil odit officiis quia sed similique
            veniam voluptate? Accusantium aliquam aspernatur earum eligendi
            exercitationem, numquam quo repudiandae rerum saepe similique.
            Aspernatur consequatur consequuntur cupiditate dicta dolorem, enim
            eum incidunt magni maiores minus nisi odio quas ratione similique
            sit tempora totam vel voluptatum. Aliquam autem beatae blanditiis
            dolores eius eveniet ex excepturi illo laboriosam molestiae
            necessitatibus nobis officiis placeat, quaerat quisquam sapiente
            suscipit temporibus ullam ut vitae. Deleniti eveniet ratione
            repellendus rerum sit sunt voluptatibus. Accusantium aliquam
            architecto blanditiis consectetur eaque earum, eveniet, excepturi
            labore nam nobis quos ratione reiciendis sapiente suscipit
            voluptatum. Dolore inventore nemo placeat quae quis, velit!
          </Text>
        </View>
      </Pressable>

      <Pressable>
        <Text>
          Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquam,
          architecto culpa ea explicabo fugiat incidunt ipsa iste labore
          laboriosam minima, nihil officiis perferendis porro possimus quo
          saepe, sit soluta vitae! Culpa eos non nulla!
        </Text>
      </Pressable>
    </View>
  );
}