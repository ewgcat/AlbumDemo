
package com.lishuaihua.album.api.choice;


public interface Choice<Multiple, Single> {

    /**
     * Multiple choice.
     */
    Multiple multipleChoice();

    /**
     * Single choice.
     */
    Single singleChoice();

}