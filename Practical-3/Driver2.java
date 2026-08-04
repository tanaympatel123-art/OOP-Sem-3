public class Driver2 {

    public static void main(String[] args) {

        Card[] cards = new Card[5];

        Card[] newCards = {
            new Card("Ace", "Spades"),
            new Card("Queen", "Hearts"),
            new Card("King", "Clubs"),
            new Card("Ace", "Spades"),   // Duplicate
            new Card("10", "Diamonds")
        };

        int count = 0;
        boolean duplicateFound = false;

        for (int i = 0; i < newCards.length; i++) {

            boolean isDuplicate = false;

            // Check against previously added cards
            for (int j = 0; j < count; j++) {

                if (newCards[i].equals(cards[j])) {
                    System.out.println("Duplicate found: " + newCards[i]);
                    isDuplicate = true;
                    duplicateFound = true;
                    break;
                }
            }

            if (!isDuplicate) {
                cards[count] = newCards[i];
                count++;
            }

            if (duplicateFound) {
                break;
            }
        }
    }
}