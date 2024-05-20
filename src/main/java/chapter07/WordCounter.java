package chapter07;

public class WordCounter {
    private final int counter;
    private final boolean lastSpace;
    public WordCounter(int counter, boolean lastSpace){
        this.counter = counter;
        this.lastSpace = lastSpace;
    }
    public WordCounter accumlate(Character c){
        if(Character.isWhitespace(c)){ // 공백
            return lastSpace ? this : new WordCounter(counter, true);
        }
        else { // 공백 아님
            return lastSpace ? new WordCounter(counter + 1, false) : this;
        }
    }

    public WordCounter combine(WordCounter wordCounter) {
        return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
    }
    public int getCounter(){
        return counter;
    }

    public static int countWordsIteratively(String s) {
        int counter = 0;
        boolean lastSpace = true;
        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) {
                lastSpace = true;
            }
            else{
                if(lastSpace) counter++;
                lastSpace = false;
            }
        }
        return counter;
    }


}
