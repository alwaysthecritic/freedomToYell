package samcarr.freedomtoyell

object Usage {
    val Message =
"""#Usage: freedomToYell <tp_export> <old_host> <new_host> <tp_article_prefix> <output_dir>
   #For example:
   #  freedomToYell export.txt www.old.com new.net blog/ migrated
   #
   #FreedomToYell is a tool to help migrate a Typepad blog to WordPress.
   #Export your blog content from Typepad to get a file containing all the posts and comments.
   #Run this tool, which takes that file <tp_export>, and writes out a new version to <output_dir>
   #with article and image URIs corrected and all images downloaded. Import the new file with the
   #standard WordPress Typepad importer and upload the images directory 'tp' to your wp-content
   #directory on the server (probably via FTP).
   #This assumes that you have setup WordPress with the same permalink format for posts."""
        .stripMargin('#')
}