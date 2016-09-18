// Generated from Micro.g4 by ANTLR 4.5.3
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MicroLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, KEYWORD=4, IDENTIFIER=5, INTLITERAL=6, FLOATLITERAL=7, 
		COMMENT=8, STRINGLITERAL=9, OPERATOR=10, WHITESPACE=11;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "KEYWORD", "IDENTIFIER", "INTLITERAL", "FLOATLITERAL", 
		"COMMENT", "STRINGLITERAL", "OPERATOR", "WHITESPACE"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'PROGRAM'", "'BEGIN'", "'END'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, "KEYWORD", "IDENTIFIER", "INTLITERAL", "FLOATLITERAL", 
		"COMMENT", "STRINGLITERAL", "OPERATOR", "WHITESPACE"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public MicroLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Micro.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\r\u00d7\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5\u008d\n\5\3"+
		"\6\3\6\7\6\u0091\n\6\f\6\16\6\u0094\13\6\3\7\6\7\u0097\n\7\r\7\16\7\u0098"+
		"\3\b\6\b\u009c\n\b\r\b\16\b\u009d\3\b\3\b\7\b\u00a2\n\b\f\b\16\b\u00a5"+
		"\13\b\3\b\3\b\6\b\u00a9\n\b\r\b\16\b\u00aa\5\b\u00ad\n\b\3\t\3\t\3\t\3"+
		"\t\7\t\u00b3\n\t\f\t\16\t\u00b6\13\t\3\t\3\t\3\t\3\t\3\n\3\n\7\n\u00be"+
		"\n\n\f\n\16\n\u00c1\13\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3"+
		"\13\3\13\3\13\5\13\u00cf\n\13\3\f\6\f\u00d2\n\f\r\f\16\f\u00d3\3\f\3\f"+
		"\2\2\r\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\3\2\t\4\2C\\"+
		"c|\5\2\62;C\\c|\3\2\f\f\3\2$$\6\2,-//\61\61??\6\2*+..=>@@\5\2\13\f\17"+
		"\17\"\"\u00f7\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2"+
		"\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2"+
		"\27\3\2\2\2\3\31\3\2\2\2\5!\3\2\2\2\7\'\3\2\2\2\t\u008c\3\2\2\2\13\u008e"+
		"\3\2\2\2\r\u0096\3\2\2\2\17\u00ac\3\2\2\2\21\u00ae\3\2\2\2\23\u00bb\3"+
		"\2\2\2\25\u00ce\3\2\2\2\27\u00d1\3\2\2\2\31\32\7R\2\2\32\33\7T\2\2\33"+
		"\34\7Q\2\2\34\35\7I\2\2\35\36\7T\2\2\36\37\7C\2\2\37 \7O\2\2 \4\3\2\2"+
		"\2!\"\7D\2\2\"#\7G\2\2#$\7I\2\2$%\7K\2\2%&\7P\2\2&\6\3\2\2\2\'(\7G\2\2"+
		"()\7P\2\2)*\7F\2\2*\b\3\2\2\2+,\7R\2\2,-\7T\2\2-.\7Q\2\2./\7I\2\2/\60"+
		"\7T\2\2\60\61\7C\2\2\61\u008d\7O\2\2\62\63\7D\2\2\63\64\7G\2\2\64\65\7"+
		"I\2\2\65\66\7K\2\2\66\u008d\7P\2\2\678\7G\2\289\7P\2\29\u008d\7F\2\2:"+
		";\7H\2\2;<\7W\2\2<=\7P\2\2=>\7E\2\2>?\7V\2\2?@\7K\2\2@A\7Q\2\2A\u008d"+
		"\7P\2\2BC\7T\2\2CD\7G\2\2DE\7C\2\2E\u008d\7F\2\2FG\7Y\2\2GH\7T\2\2HI\7"+
		"K\2\2IJ\7V\2\2J\u008d\7G\2\2KL\7K\2\2L\u008d\7H\2\2MN\7G\2\2NO\7N\2\2"+
		"OP\7U\2\2PQ\7K\2\2Q\u008d\7H\2\2RS\7G\2\2ST\7P\2\2TU\7F\2\2UV\7K\2\2V"+
		"\u008d\7H\2\2WX\7F\2\2X\u008d\7Q\2\2YZ\7Y\2\2Z[\7J\2\2[\\\7K\2\2\\]\7"+
		"N\2\2]\u008d\7G\2\2^_\7E\2\2_`\7Q\2\2`a\7P\2\2ab\7V\2\2bc\7K\2\2cd\7P"+
		"\2\2de\7W\2\2e\u008d\7G\2\2fg\7D\2\2gh\7T\2\2hi\7G\2\2ij\7C\2\2j\u008d"+
		"\7M\2\2kl\7T\2\2lm\7G\2\2mn\7V\2\2no\7W\2\2op\7T\2\2p\u008d\7P\2\2qr\7"+
		"K\2\2rs\7P\2\2s\u008d\7V\2\2tu\7X\2\2uv\7Q\2\2vw\7K\2\2w\u008d\7F\2\2"+
		"xy\7U\2\2yz\7V\2\2z{\7T\2\2{|\7K\2\2|}\7P\2\2}\u008d\7I\2\2~\177\7H\2"+
		"\2\177\u0080\7N\2\2\u0080\u0081\7Q\2\2\u0081\u0082\7C\2\2\u0082\u008d"+
		"\7V\2\2\u0083\u0084\7V\2\2\u0084\u0085\7T\2\2\u0085\u0086\7W\2\2\u0086"+
		"\u008d\7G\2\2\u0087\u0088\7H\2\2\u0088\u0089\7C\2\2\u0089\u008a\7N\2\2"+
		"\u008a\u008b\7U\2\2\u008b\u008d\7G\2\2\u008c+\3\2\2\2\u008c\62\3\2\2\2"+
		"\u008c\67\3\2\2\2\u008c:\3\2\2\2\u008cB\3\2\2\2\u008cF\3\2\2\2\u008cK"+
		"\3\2\2\2\u008cM\3\2\2\2\u008cR\3\2\2\2\u008cW\3\2\2\2\u008cY\3\2\2\2\u008c"+
		"^\3\2\2\2\u008cf\3\2\2\2\u008ck\3\2\2\2\u008cq\3\2\2\2\u008ct\3\2\2\2"+
		"\u008cx\3\2\2\2\u008c~\3\2\2\2\u008c\u0083\3\2\2\2\u008c\u0087\3\2\2\2"+
		"\u008d\n\3\2\2\2\u008e\u0092\t\2\2\2\u008f\u0091\t\3\2\2\u0090\u008f\3"+
		"\2\2\2\u0091\u0094\3\2\2\2\u0092\u0090\3\2\2\2\u0092\u0093\3\2\2\2\u0093"+
		"\f\3\2\2\2\u0094\u0092\3\2\2\2\u0095\u0097\4\62;\2\u0096\u0095\3\2\2\2"+
		"\u0097\u0098\3\2\2\2\u0098\u0096\3\2\2\2\u0098\u0099\3\2\2\2\u0099\16"+
		"\3\2\2\2\u009a\u009c\4\62;\2\u009b\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d"+
		"\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u009f\3\2\2\2\u009f\u00a3\7\60"+
		"\2\2\u00a0\u00a2\4\62;\2\u00a1\u00a0\3\2\2\2\u00a2\u00a5\3\2\2\2\u00a3"+
		"\u00a1\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4\u00ad\3\2\2\2\u00a5\u00a3\3\2"+
		"\2\2\u00a6\u00a8\7\60\2\2\u00a7\u00a9\4\62;\2\u00a8\u00a7\3\2\2\2\u00a9"+
		"\u00aa\3\2\2\2\u00aa\u00a8\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab\u00ad\3\2"+
		"\2\2\u00ac\u009b\3\2\2\2\u00ac\u00a6\3\2\2\2\u00ad\20\3\2\2\2\u00ae\u00af"+
		"\7/\2\2\u00af\u00b0\7/\2\2\u00b0\u00b4\3\2\2\2\u00b1\u00b3\n\4\2\2\u00b2"+
		"\u00b1\3\2\2\2\u00b3\u00b6\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b4\u00b5\3\2"+
		"\2\2\u00b5\u00b7\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b7\u00b8\7\f\2\2\u00b8"+
		"\u00b9\3\2\2\2\u00b9\u00ba\b\t\2\2\u00ba\22\3\2\2\2\u00bb\u00bf\7$\2\2"+
		"\u00bc\u00be\n\5\2\2\u00bd\u00bc\3\2\2\2\u00be\u00c1\3\2\2\2\u00bf\u00bd"+
		"\3\2\2\2\u00bf\u00c0\3\2\2\2\u00c0\u00c2\3\2\2\2\u00c1\u00bf\3\2\2\2\u00c2"+
		"\u00c3\7$\2\2\u00c3\24\3\2\2\2\u00c4\u00c5\7<\2\2\u00c5\u00cf\7?\2\2\u00c6"+
		"\u00cf\t\6\2\2\u00c7\u00c8\7#\2\2\u00c8\u00cf\7?\2\2\u00c9\u00cf\t\7\2"+
		"\2\u00ca\u00cb\7>\2\2\u00cb\u00cf\7?\2\2\u00cc\u00cd\7@\2\2\u00cd\u00cf"+
		"\7?\2\2\u00ce\u00c4\3\2\2\2\u00ce\u00c6\3\2\2\2\u00ce\u00c7\3\2\2\2\u00ce"+
		"\u00c9\3\2\2\2\u00ce\u00ca\3\2\2\2\u00ce\u00cc\3\2\2\2\u00cf\26\3\2\2"+
		"\2\u00d0\u00d2\t\b\2\2\u00d1\u00d0\3\2\2\2\u00d2\u00d3\3\2\2\2\u00d3\u00d1"+
		"\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5\u00d6\b\f\2\2\u00d6"+
		"\30\3\2\2\2\16\2\u008c\u0092\u0098\u009d\u00a3\u00aa\u00ac\u00b4\u00bf"+
		"\u00ce\u00d3\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}