import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;


public class annotator extends JCasAnnotator_ImplBase {
	/**
	 * process(JCas aCas) get the input file content into CAS, split them by sentence
	 * Also, get the ID and content seperately and then store them seperately
	 */
	@Override
	public void process(JCas aCas) throws AnalysisEngineProcessException {
		// TODO Auto-generated method stub
		JCas jcas = aCas;
		String text = jcas.getDocumentText().trim();
		
		//System.out.println(text);
		//split by line
		String contents[] = text.split("\\n");
		
		for(int i = 0; i < contents.length; i++){
			//what if we do not know the length of ID
			//making things safer
			int space = contents[i].indexOf(" ");
			
			String sentenceId = contents[i].substring(0, space);
			String sentenceContent = contents[i].substring(space).trim();
			
			sentence sen = new sentence(jcas);
			sen.setID(sentenceId);
			sen.setContent(sentenceContent);
			sen.addToIndexes();
		
		}	
	}

}
