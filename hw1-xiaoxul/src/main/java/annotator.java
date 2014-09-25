import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;


public class annotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas aCas) throws AnalysisEngineProcessException {
		// TODO Auto-generated method stub
		JCas jcas = aCas;
		String text = jcas.getDocumentText().trim();
		
		System.out.println(text);
		
		String contents[] = text.split("\\n");
		for(int i = 0; i < contents.length; i++){
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
