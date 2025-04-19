import {
  StyleSheet,
  SafeAreaView,
  Platform,
  ScrollView,
  FlatList,
  View,
  Text,
} from "react-native";
import PokemonCard from "@/components/pokemon-card";

export const pokemonData = [
  {
    name: "charmander",
    image: require("../assets/images/charmander.png"),
    type: "Fire",
    hp: 300,
    moves: ["Scratch", "Ember", "Growl", "Leer"],
    weaknesses: ["Water", "Rock"],
  },
  {
    name: "Squirtle",
    image: require("../assets/images/squirtle.png"), // Replace with the actual image path
    type: "Water",
    hp: 44,
    moves: ["Tackle", "Water Gun", "Tail Whip", "Withdraw"],
    weaknesses: ["Electric", "Grass"],
  },

  {
    name: "Bulbasaur",
    image: require("../assets/images/bulbasaur.png"), // Replace with the actual image path
    type: "Grass",
    hp: 45,
    moves: ["Tackle", "Vine Whip", "Growl", "Leech Seed"],
    weaknesses: ["Fire", "Ice", "Flying", "Psychic"],
  },
];

export default function Index() {
  return (
    <SafeAreaView style={styles.container}>
      <FlatList
        data={pokemonData}
        renderItem={({ item }) => <PokemonCard {...item} />}
        // horizontal={true}
        keyExtractor={(item) => item.name}
        ItemSeparatorComponent={(t) => <View style={{ marginTop: 50 }}></View>}
        ListEmptyComponent={<Text>List is empty....</Text>}
        ListHeaderComponent={<Text>Pokemon's</Text>}
        ListFooterComponent={<Text>Pokemon' List end</Text>}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f5f5f5",
    alignItems: "center",
    justifyContent: "center",
    paddingTop: Platform.OS === "android" ? 22 : 0,
  },
});

/*
Rendering a list using a map is not performent instead use

    <FlatList
      data={pokemonData}
      render={({item})=>(

    //item represent the current item to be rendered

    //and return the JSX you want to render
      )}

      // horizontal={true}

      //return the unique id for key
      keyExtractor={(item)=>item.id.toString()}

       //separator between list components after first item and before last item
       ItemSeparatorComponent={(t) => <View style={{ marginTop: 50 }}></View>}

       //it will render when list is empty
        ListEmptyComponent={<Text>List is empty....</Text>}

        //header for the list
        ListHeaderComponent={<Text>Pokemon's</Text>}

        //footer for the list
        ListFooterComponent={<Text>Pokemon' List end</Text>}
        
      />


      //if you want to list with different sections you can use this (check docs)
      <SectionList

        //section separator
        SectionSeparatorComponent={()=><Text>Pokemon's</Text>}
      />

 */