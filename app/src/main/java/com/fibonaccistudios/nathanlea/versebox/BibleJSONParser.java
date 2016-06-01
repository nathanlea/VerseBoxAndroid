package com.fibonaccistudios.nathanlea.versebox;

/**
 * Created by ngoalie on 5/31/2016.
 */
public class BibleJSONParser {
    public Bible bible;

    public BibleJSONParser(String JSONfile) {
        String jsonString = JSONfile.replaceAll("\\s+", "");

        //New Bible
        bible = new Bible();

        int stringLeftToIndex = JSONfile.length();
        int currentPosInString = 0;

        //Pull off the first characters
        currentPosInString += 2;
        stringLeftToIndex -= 2;

        //Set list counters for levels of listing
        int bookLevel = -1;

        //Starting at the chapter level
        while (stringLeftToIndex > 0) {
            //Get the chapter
            String book;
            String bookBlock = jsonString.substring(currentPosInString, currentPosInString+10);
            if(bookBlock.charAt(bookBlock.length()-1)>=65 && bookBlock.charAt(bookBlock.length()-1) <= 122) {
                bookBlock = jsonString.substring(currentPosInString, currentPosInString+15);
            }
            String[] bookSplit = bookBlock.split(":");
            book = bookSplit[0];
            book = book.replaceAll("\"", "");
            book = book.replaceAll(":", "");
            book = book.replaceAll("\\{", "");
            //Update counters
            currentPosInString += book.length() + 3;
            stringLeftToIndex -= book.length() + 3;

            //Create Object
            bible.books.add(new BibleBook(book));
            bookLevel++;

            while (stringLeftToIndex > 0) {
                String chapter = "";
                String chapterBlock = jsonString.substring(currentPosInString, currentPosInString+10);

                chapter = chapterBlock.split(",")[0];// }
                chapter = chapter.replaceAll("\"", "");
                chapter = chapter.replaceAll(",", "");
                chapter = chapter.replaceAll("\\{", "");
                if(chapter.charAt(chapter.length()-1)=='}') {
                    chapter = chapter.substring(0, chapter.length()-1);
                    currentPosInString--;
                }

                String[] countSplit = chapter.split(":");
                //Update counters
                currentPosInString += chapter.length() + 4;
                stringLeftToIndex -= chapter.length() + 4;

                //Create Object
                bible.books.get(bookLevel).chapter.add(new BibleChapter(Integer.parseInt(countSplit[0]), Integer.parseInt(countSplit[1])));

                if (jsonString.charAt(currentPosInString) == ',') {
                    currentPosInString++;
                    continue;
                } else {
                    break;
                }
            }
            currentPosInString+=2;
            if (jsonString.charAt(currentPosInString) == ',') {
                currentPosInString+=2;
                continue;
            } else {
                break;
            }
        }
    }
    public Bible getBible() {
        return bible;
    }
}
