package testsuite.unittests;

import java.io.*;

import com.opensymphony.module.sitemesh.parser.FastPageParser;

/**
 * @author Hani Suleiman (hani@formicary.net)
 * Date: Nov 22 2003
 * Time: 12:09:26 AM
 */
public class ParserGrinder
{
  public static void main(String[] args) throws IOException, InterruptedException
  {
    if(args.length==0)
    {
      System.err.println("Usage: java " + ParserGrinder.class.getName() + " <html file>");
      System.exit(1);
    }
    File file = new File(args[0]);
    if(!file.exists() || file.isDirectory())
    {
      System.err.println("File " + args[0] + " does not exist or is a directory");
      System.exit(1);
    }
    FileInputStream fis = new FileInputStream(file);
    byte[] contents = new byte[(int)file.length()];
    fis.read(contents);
    String page = new String(contents);
    final char[] chars = page.toCharArray();
    //warm up parser

    for(int i=0;i<10;i++)
    {
      FastPageParser parser = new FastPageParser();
      parser.parse(chars);
    }

    //now go crazy
    Thread[] threads = new Thread[10];
    final int passes = 200;
    for(int i=0;i<threads.length;i++)
    {
      threads[i] = new Thread(new Runnable()
      {
        public void run()
        {
          for(int j=0;j<passes;j++)
          {
            FastPageParser parser = new FastPageParser();
            try
            {
              parser.parse(chars);
            }
            catch(IOException e)
            {
              e.printStackTrace();
            }
          }
        }
      });
    }
    //we do this here instead of above just to not count the thread creation overhead
    long now = System.currentTimeMillis();
    for(int i=0;i<threads.length;i++)
    {
      threads[i].start();
    }

    for(int i = 0; i < threads.length; i++)
    {
      threads[i].join();
    }
    System.out.println("time taken " + (System.currentTimeMillis()-now) + " for " + (threads.length * passes) + " parses.");
  }
}