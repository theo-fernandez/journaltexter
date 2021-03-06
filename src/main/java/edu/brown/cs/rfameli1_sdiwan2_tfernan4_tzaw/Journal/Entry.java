package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to hold data for a given entry in the journal.
 *
 * @param <T> a class that implements the JournalText interface
 */
public class Entry<T extends JournalText> {
  private final Integer id;
  private final LocalDate date;
  private final String stringRepresentation;
  private final List<T> questionsAndResponses;
  private final String title;
  private final List<String> tags;
  private final Double totalSentiment = 0.0;
  private final Integer numberOfQuestions = 0;

  /**
   * Constructs an entry using a string representation of the entry.
   *
   * @param id                   a unique id for the entry
   * @param date                 the date the entry was created
   * @param entryTitle           the title of the entry
   * @param stringRepresentation a string representation representing the text of the entry
   * @param tags  the tags that were found in user's responses throughout the entry
   */
  public Entry(Integer id, LocalDate date, String entryTitle, String stringRepresentation,
               List<String> tags) {
    // Responses will be represented as {response}
    // Questions will be represented as {@question}
    this.id = id;
    this.date = date;
    this.title = entryTitle;
    List<T> questionsResponses = new ArrayList<>();
    Pattern regexParse = Pattern.compile("\\{([^{}]*)\\}");
    Matcher m = regexParse.matcher(stringRepresentation);
    while (m.find()) {
      String questionOrResponseText = m.group();
      questionOrResponseText =
          questionOrResponseText.substring(1, questionOrResponseText.length() - 1);
      if (!(questionOrResponseText.length() == 0) && questionOrResponseText.charAt(0) == '@') {
        questionsResponses.add(
            // Add the question string without the @ symbol
            (T) new Question(questionOrResponseText.substring(1)));
      } else {
        questionsResponses.add((T) new Response(questionOrResponseText));
      }
    }
    this.questionsAndResponses = questionsResponses;
    this.stringRepresentation = stringRepresentation;
    this.tags = tags;
  }

  /**
   * Parameterized constructor for an entry.
   *
   * @param id                    a unique id for the entry
   * @param date                  the date the entry was created
   * @param entryTitle            the entryTitle of the entry
   * @param questionsAndResponses a List of Questions and Responses
   * @param tags the tags that were found in responses throughout the entry
   */
  public Entry(Integer id, LocalDate date, String entryTitle, List<T> questionsAndResponses,
               List<String> tags) {
    this.id = id;
    this.date = date;
    this.title = entryTitle;
    this.questionsAndResponses = questionsAndResponses;
    StringBuilder stringRep = new StringBuilder();
    // Iterate through every question/response
    for (T questionOrResponse : questionsAndResponses) {
      // Concatenate the questionOrResponse's stringRepresentation
      stringRep.append(questionOrResponse.stringRepresentation());
    }
    this.stringRepresentation = stringRep.toString();
    this.tags = tags;
  }

  /**
   * Retrieves the date of the entry.
   *
   * @return a date in Java's Date format
   */
  public LocalDate getDate() {
    return this.date;
  }

  /**
   * Retrieves the ID of this entry.
   *
   * @return this entry's id
   */
  public Integer getId() {
    return this.id;
  }

  /**
   * Gets the string representation of the Entry.
   *
   * @return a formatted String such as "\{question\}\{response\}"
   */
  public String getStringRepresentation() {
    return this.stringRepresentation;
  }

  /**
   * Gets the List of JournalText that represents the Questions and Responses in the Entry.
   *
   * @return a List of JournalText
   */
  public List<T> getQuestionsAndResponses() {
    return new ArrayList<>(this.questionsAndResponses);
  }

  /**
   * Gets the tags from jtDatabase based on the most common words found in all responses.
   *
   * @return a Map of tags and their frequencies in responses
   */
  public List<String> getTags() {
    // CURRENTLY NOT FUNCTIONAL
    return new LinkedList<>(this.tags); // <== instantiate this when retrieving from the database
  }

  /**
   * Gets the title of the entry.
   * @return a String representing the entry's title
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * Gets the total sentiment of all responses in the entry.
   *
   * @return a value between 0 and 1 representing the overall weighted sentiment of all responses
   */
  public Double getWeightedSentiment() {
    return 0.;
//    return this.totalSentiment / this.numberOfQuestions;
  }

}
