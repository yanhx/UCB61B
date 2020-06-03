import org.junit.Test;

import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        String s1 = "abcdedcba";
        assertTrue(palindrome.isPalindrome(s1));
        String s2 = "asdasdasaseee";
        assertFalse(palindrome.isPalindrome(s2));
        String s3 = "a";
        assertTrue(palindrome.isPalindrome(s3));
        String s4 = "aa";
        assertTrue(palindrome.isPalindrome(s4));
    }

    @Test
    public void testIsPalindromeCC() {
        String s1 = "flake";
        CharacterComparator cc = new OffByOne();
        assertTrue(palindrome.isPalindrome(s1, cc));
    }
}
