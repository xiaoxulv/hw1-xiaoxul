import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;
import com.aliasi.util.AbstractExternalizable;


public class geneAnalysis extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas aCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    JCas jcas = aCas;
    int count = 0;
    
    File modelFile = new File("src/main/resources/data/ne-en-bio-genetag.HmmChunker");
    System.out.println("Reading chunker from file=" + modelFile);
    FSIterator it = jcas.getAnnotationIndex(sentence.type).iterator();
    Chunker chunker = null;
   
    try {
      chunker = (Chunker) AbstractExternalizable.readObject(modelFile);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    while(it.hasNext()){
        sentence ann = (sentence)it.get();  
        String sen = ann.getContent();
        String id = ann.getID();
        Chunking chunking = chunker.chunk(ann.getContent());     
        //System.out.println("Chunking=" + chunking);
       
        String gene;
        Set<Chunk> set = chunking.chunkSet();
        Iterator itor = set.iterator();
        while(itor.hasNext()){
            Chunk c = (Chunk) itor.next();
            gene = (sen.substring(c.start(), c.end()));
            //System.out.println(c.start());
            //System.out.println(c.end());
            System.out.println(gene);
            int begin = c.start() ;
            int end = c.end();
            begin = begin - countBlank(sen.substring(0,begin)) ;
            end = begin + gene.length() - countBlank(gene) - 1;
            
            Gene gt = new Gene(aCas);
            gt.setID(id);
            gt.setContent(gene);
            gt.setBegin(begin);
            gt.setEnd(end);
            gt.addToIndexes();
           
        }
        System.out.println(count++);
        it.next();
        
    }
    
    
    
//    //not use Pos... ANYMORE!
//    String sentenceIdentifier = "";
//    String sentenceText = "";
//    System.out.println("Processing GENE");
//    PosTagNamedEntityRecognizer Tagger = null;
//    try {
//      Tagger = new PosTagNamedEntityRecognizer();
//    } catch (ResourceInitializationException e) {
//    // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
//    
//    
//    FSIterator it = jcas.getAnnotationIndex(sentence.type).iterator();
//    while (it.hasNext()) {
//
//      sentence annotation = (sentence) it.next();
//      sentenceIdentifier = annotation.getID();
//      sentenceText = annotation.getContent();
//      
//      Map<Integer, Integer> occurences = Tagger.getGeneSpans(sentenceText);
//      int begin;
//      int end;
//      String gene;
//      for (Map.Entry<Integer, Integer> entry : occurences.entrySet())
//      {
//          begin = entry.getKey();
//          end = entry.getValue();
//          gene = sentenceText.substring(begin, end);
//          begin = begin - countWhiteSpaces(sentenceText.substring(0,begin)) ;
//          end = begin + gene.length() - countWhiteSpaces(gene) - 1;
//          
//          if(genes.findGene(gene) == true ){
//            Genetag anno = new Genetag(jcas);
//            anno.setBegin(begin);
//            anno.setEnd(end);
//            anno.setID(sentenceIdentifier);
//            anno.setContent(gene);
//            anno.addToIndexes();
//          }
//      }
//     
      
    }
    
    

  
  private int countBlank(String phrase){
    int countBlank = 0;
    for(int i=0; i<phrase.length(); i++) {
        if(Character.isWhitespace(phrase.charAt(i))) {
            countBlank++;
        }
     }
    return countBlank;
  }
  
 
}