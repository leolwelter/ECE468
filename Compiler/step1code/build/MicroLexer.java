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
		KEYWORD=1, IDENTIFIER=2, INTLITERAL=3, FLOATLITERAL=4, COMMENT=5, STRINGLITERAL=6, 
		OPERATOR=7, WHITESPACE=8;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"KEYWORD", "IDENTIFIER", "INTLITERAL", "FLOATLITERAL", "COMMENT", "STRINGLITERAL", 
		"OPERATOR", "WHITESPACE"
	};

	private static final String[] _LITERAL_NAMES = {
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "KEYWORD", "IDENTIFIER", "INTLITERAL", "FLOATLITERAL", "COMMENT", 
		"STRINGLITERAL", "OPERATOR", "WHITESPACE"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\n\u00bf\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2u\n\2\3\3\3\3\7\3y\n\3\f\3\16\3|\13\3\3"+
		"\4\6\4\177\n\4\r\4\16\4\u0080\3\5\6\5\u0084\n\5\r\5\16\5\u0085\3\5\3\5"+
		"\7\5\u008a\n\5\f\5\16\5\u008d\13\5\3\5\3\5\6\5\u0091\n\5\r\5\16\5\u0092"+
		"\5\5\u0095\n\5\3\6\3\6\3\6\3\6\7\6\u009b\n\6\f\6\16\6\u009e\13\6\3\6\3"+
		"\6\3\6\3\6\3\7\3\7\7\7\u00a6\n\7\f\7\16\7\u00a9\13\7\3\7\3\7\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\b\u00b7\n\b\3\t\6\t\u00ba\n\t\r\t\16"+
		"\t\u00bb\3\t\3\t\2\2\n\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\3\2\t\4\2C\\"+
		"c|\5\2\62;C\\c|\3\2\f\f\3\2$$\6\2,-//\61\61??\6\2*+..=>@@\5\2\13\f\17"+
		"\17\"\"\u00df\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2"+
		"\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\3t\3\2\2\2\5v\3\2\2\2\7~\3"+
		"\2\2\2\t\u0094\3\2\2\2\13\u0096\3\2\2\2\r\u00a3\3\2\2\2\17\u00b6\3\2\2"+
		"\2\21\u00b9\3\2\2\2\23\24\7R\2\2\24\25\7T\2\2\25\26\7Q\2\2\26\27\7I\2"+
		"\2\27\30\7T\2\2\30\31\7C\2\2\31u\7O\2\2\32\33\7D\2\2\33\34\7G\2\2\34\35"+
		"\7I\2\2\35\36\7K\2\2\36u\7P\2\2\37 \7G\2\2 !\7P\2\2!u\7F\2\2\"#\7H\2\2"+
		"#$\7W\2\2$%\7P\2\2%&\7E\2\2&\'\7V\2\2\'(\7K\2\2()\7Q\2\2)u\7P\2\2*+\7"+
		"T\2\2+,\7G\2\2,-\7C\2\2-u\7F\2\2./\7Y\2\2/\60\7T\2\2\60\61\7K\2\2\61\62"+
		"\7V\2\2\62u\7G\2\2\63\64\7K\2\2\64u\7H\2\2\65\66\7G\2\2\66\67\7N\2\2\67"+
		"8\7U\2\289\7K\2\29u\7H\2\2:;\7G\2\2;<\7P\2\2<=\7F\2\2=>\7K\2\2>u\7H\2"+
		"\2?@\7F\2\2@u\7Q\2\2AB\7Y\2\2BC\7J\2\2CD\7K\2\2DE\7N\2\2Eu\7G\2\2FG\7"+
		"E\2\2GH\7Q\2\2HI\7P\2\2IJ\7V\2\2JK\7K\2\2KL\7P\2\2LM\7W\2\2Mu\7G\2\2N"+
		"O\7D\2\2OP\7T\2\2PQ\7G\2\2QR\7C\2\2Ru\7M\2\2ST\7T\2\2TU\7G\2\2UV\7V\2"+
		"\2VW\7W\2\2WX\7T\2\2Xu\7P\2\2YZ\7K\2\2Z[\7P\2\2[u\7V\2\2\\]\7X\2\2]^\7"+
		"Q\2\2^_\7K\2\2_u\7F\2\2`a\7U\2\2ab\7V\2\2bc\7T\2\2cd\7K\2\2de\7P\2\2e"+
		"u\7I\2\2fg\7H\2\2gh\7N\2\2hi\7Q\2\2ij\7C\2\2ju\7V\2\2kl\7V\2\2lm\7T\2"+
		"\2mn\7W\2\2nu\7G\2\2op\7H\2\2pq\7C\2\2qr\7N\2\2rs\7U\2\2su\7G\2\2t\23"+
		"\3\2\2\2t\32\3\2\2\2t\37\3\2\2\2t\"\3\2\2\2t*\3\2\2\2t.\3\2\2\2t\63\3"+
		"\2\2\2t\65\3\2\2\2t:\3\2\2\2t?\3\2\2\2tA\3\2\2\2tF\3\2\2\2tN\3\2\2\2t"+
		"S\3\2\2\2tY\3\2\2\2t\\\3\2\2\2t`\3\2\2\2tf\3\2\2\2tk\3\2\2\2to\3\2\2\2"+
		"u\4\3\2\2\2vz\t\2\2\2wy\t\3\2\2xw\3\2\2\2y|\3\2\2\2zx\3\2\2\2z{\3\2\2"+
		"\2{\6\3\2\2\2|z\3\2\2\2}\177\4\62;\2~}\3\2\2\2\177\u0080\3\2\2\2\u0080"+
		"~\3\2\2\2\u0080\u0081\3\2\2\2\u0081\b\3\2\2\2\u0082\u0084\4\62;\2\u0083"+
		"\u0082\3\2\2\2\u0084\u0085\3\2\2\2\u0085\u0083\3\2\2\2\u0085\u0086\3\2"+
		"\2\2\u0086\u0087\3\2\2\2\u0087\u008b\7\60\2\2\u0088\u008a\4\62;\2\u0089"+
		"\u0088\3\2\2\2\u008a\u008d\3\2\2\2\u008b\u0089\3\2\2\2\u008b\u008c\3\2"+
		"\2\2\u008c\u0095\3\2\2\2\u008d\u008b\3\2\2\2\u008e\u0090\7\60\2\2\u008f"+
		"\u0091\4\62;\2\u0090\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0090\3\2"+
		"\2\2\u0092\u0093\3\2\2\2\u0093\u0095\3\2\2\2\u0094\u0083\3\2\2\2\u0094"+
		"\u008e\3\2\2\2\u0095\n\3\2\2\2\u0096\u0097\7/\2\2\u0097\u0098\7/\2\2\u0098"+
		"\u009c\3\2\2\2\u0099\u009b\n\4\2\2\u009a\u0099\3\2\2\2\u009b\u009e\3\2"+
		"\2\2\u009c\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d\u009f\3\2\2\2\u009e"+
		"\u009c\3\2\2\2\u009f\u00a0\7\f\2\2\u00a0\u00a1\3\2\2\2\u00a1\u00a2\b\6"+
		"\2\2\u00a2\f\3\2\2\2\u00a3\u00a7\7$\2\2\u00a4\u00a6\n\5\2\2\u00a5\u00a4"+
		"\3\2\2\2\u00a6\u00a9\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a7\u00a8\3\2\2\2\u00a8"+
		"\u00aa\3\2\2\2\u00a9\u00a7\3\2\2\2\u00aa\u00ab\7$\2\2\u00ab\16\3\2\2\2"+
		"\u00ac\u00ad\7<\2\2\u00ad\u00b7\7?\2\2\u00ae\u00b7\t\6\2\2\u00af\u00b0"+
		"\7#\2\2\u00b0\u00b7\7?\2\2\u00b1\u00b7\t\7\2\2\u00b2\u00b3\7>\2\2\u00b3"+
		"\u00b7\7?\2\2\u00b4\u00b5\7@\2\2\u00b5\u00b7\7?\2\2\u00b6\u00ac\3\2\2"+
		"\2\u00b6\u00ae\3\2\2\2\u00b6\u00af\3\2\2\2\u00b6\u00b1\3\2\2\2\u00b6\u00b2"+
		"\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b7\20\3\2\2\2\u00b8\u00ba\t\b\2\2\u00b9"+
		"\u00b8\3\2\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00b9\3\2\2\2\u00bb\u00bc\3\2"+
		"\2\2\u00bc\u00bd\3\2\2\2\u00bd\u00be\b\t\2\2\u00be\22\3\2\2\2\16\2tz\u0080"+
		"\u0085\u008b\u0092\u0094\u009c\u00a7\u00b6\u00bb\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}