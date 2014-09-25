import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceProcessException;
import org.xml.sax.SAXException;


public class consumer extends CasConsumer_ImplBase {
	private BufferedWriter buf;
	public static final String PARAM_OUTPUTDIR = "outputfile";
	/**
	 * process() write the gene ID,name,index into the disk file, which are got from geneAnalysis. 
	 */
	@Override
	public void processCas(CAS aCAS) throws ResourceProcessException {
		// TODO Auto-generated method stub
		File out = new File(((String) getConfigParameterValue(PARAM_OUTPUTDIR)).trim());
		try{
			//File out = new File("src/main/resources/data/hw1-xiaoxul.out");
			
			buf = new BufferedWriter(new FileWriter(out));
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("Innitializing the consumer~");
		JCas jcas;
	    try {
	      jcas = aCAS.getJCas();
	    } catch (CASException e) {
	      throw new ResourceProcessException(e);
	    }
	    
	    FSIterator it = jcas.getAnnotationIndex(Gene.type).iterator();
	    String geneId = "";
	    String geneContent = "";
	    int start,end = -1;
	    
	    while(it.hasNext()){
	    	Gene annotation = (Gene) it.next();
			geneId = annotation.getID();
			geneContent = annotation.getContent();			
			start = annotation.getBegin();
			end = annotation.getEnd();

			try {
				writeIntoFile(geneId, geneContent, start, end);
				System.out.println("writing~!");
			} catch (IOException e) {
				throw new ResourceProcessException(e);
			} catch (SAXException e) {
				throw new ResourceProcessException(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	    }
	}
	public void writeIntoFile(String geneIdentifier, String geneName, int start, int end)
			throws Exception {
		buf.write(geneIdentifier + "|" + start + " " + end + "|" + geneName);
		buf.newLine();
		buf.flush();
	}

}
