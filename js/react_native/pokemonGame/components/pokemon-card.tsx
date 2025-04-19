import { Image, Platform, StyleSheet, Text, View } from "react-native";
import { pokemonData } from "@/app";

const getTypeDetails = (type: string) => {
  switch (type.toLowerCase()) {
    case "electric":
      return { borderColor: "#FFD700", emoji: "⚡️" };
    case "water":
      return { borderColor: "#6493EA", emoji: "💧" };
    case "fire":
      return { borderColor: "#FF5733", emoji: "🔥" };
    case "grass":
      return { borderColor: "#66CC66", emoji: "🌿" };
    default:
      return { borderColor: "#A0A0A0", emoji: "❓" };
  }
};

type Props = (typeof pokemonData)[0];
const PokemonCard = ({ name, image, type, hp, moves, weaknesses }: Props) => {
  const { borderColor, emoji } = getTypeDetails(type);

  return (
    <View>
      <View style={styles.nameContainer}>
        <Text style={styles.name}>{name}</Text>
        <Text style={styles.hp}>{hp}</Text>
      </View>
      <Image
        style={styles.image}
        resizeMode={"contain"} //don't cop the image if we adjust the size
        source={image}
        accessibilityLabel={`${name} pokemon`}
      />
      <View style={styles.typeContainer}>
        <Text style={[styles.badge, { borderColor: borderColor }]}>
          <Text style={styles.emoji}>{emoji}</Text>
          <Text style={styles.type}>{type}</Text>
        </Text>
      </View>
      <View style={styles.movesContainer}>
        <Text style={styles.moves}>Moves: {moves.join(", ")}</Text>
      </View>
      <View style={styles.weaknessContainer}>
        <Text style={styles.weakness}>Weakness: {weaknesses.join(", ")}</Text>
      </View>
    </View>
  );
};
export default PokemonCard;
const styles = StyleSheet.create({
  card: {
    borderColor: "white",
    borderRadius: 16,
    borderWidth: 2,
    padding: 16,
    margin: 16,

    ...Platform.select({
      ios: {
        shadowOffset: { width: 2, height: 2 },
        shadowColor: "#333",
        shadowOpacity: 0.3,
        shadowRadius: 4,
      },
      android: {
        elevation: 5,
      },
    }),
  },
  nameContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 32,
  },
  name: {
    fontSize: 30,
    fontWeight: "bold",
  },
  hp: {
    fontSize: 22,
  },
  image: {
    width: "100%",
    height: 200,
    marginBottom: 16,
  },
  typeContainer: {
    alignItems: "center",
    marginBottom: 40,
  },
  badge: {
    flexDirection: "row",
    alignItems: "center",
    paddingVertical: 6,
    paddingHorizontal: 12,
    borderRadius: 20,
    borderWidth: 4,
  },
  emoji: {
    fontSize: 30,
    marginRight: 12,
  },
  type: {
    fontSize: 22,
    fontWeight: "bold",
  },
  movesContainer: {
    marginBottom: 16,
  },
  moves: {
    fontSize: 22,
    fontWeight: "bold",
  },
  weaknessContainer: {
    marginBottom: 8,
  },
  weakness: {
    fontSize: 22,
    fontWeight: "bold",
  },
});