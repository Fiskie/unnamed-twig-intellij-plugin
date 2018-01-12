package com.fisk.twig.parsing;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.Stack;

// suppress various warnings/inspections for the generated class
@SuppressWarnings ({"FieldCanBeLocal", "UnusedDeclaration", "UnusedAssignment", "AccessStaticViaInstance", "MismatchedReadAndWriteOfArray", "WeakerAccess", "SameParameterValue", "CanBeFinal", "SameReturnValue", "RedundantThrows", "ConstantConditions"})
%%

%class _TwigLexer
%implements FlexLexer
%final
%unicode
%function advance
%type IElementType

%{
    private Stack<Integer> stack = new Stack<Integer>();

    public void yypushState(int newState) {
      stack.push(yystate());
      yybegin(newState);
    }

    public void yypopState() {
      yybegin(stack.pop());
    }

    public IElementType handleContent() {
        if (yytext().toString().trim().length() == 0) {
            return TwigTokenTypes.WHITE_SPACE;
        } else {
            return TwigTokenTypes.CONTENT;
        }
    }
%}

LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\x0B\f]

// these will match the usual syntax and the whitespace controller ({{ and {{-, etc)
ExpressionOpen = \{\{-?
ExpressionClose = -?\}\}

StatementOpen = \{%-?
StatementClose = -?%\}

CommentOpen = \{#-?
CommentClose = -?#\}

Label = [A-Za-z_]\w*

%state statement
%state statement_block_tag
%state twig
%state comment
%state expression
%state hash
%%

<YYINITIAL> {
    ~"{" {
        // backtrack over any stache characters at the end of this string
        while (yylength() > 0 && yytext().subSequence(yylength() - 1, yylength()).toString().equals("{")) {
            yypushback(1);
        }

        yypushState(twig);

        if (!yytext().toString().equals("")) {
          return handleContent();
        }
    }

    // Check for anything that is not a string containing "{"; that's CONTENT
    !([^]*"{"[^]*) {
        return handleContent();
    }
}

<twig> {
    {CommentOpen} {
        yypopState(); yypushState(comment); return TwigTokenTypes.COMMENT_OPEN;
    }

    {ExpressionOpen} {
        yypopState(); yypushState(expression); return TwigTokenTypes.EXPRESSION_OPEN;
    }

    {StatementOpen} {
        yypopState(); yypushState(statement_block_tag); return TwigTokenTypes.STATEMENT_OPEN;
    }

    // FIXME: fix content lexer to consume content properly
    // Right now it's splitting CONTENT on certain delimiters (#, %, {). External fragments will work,
    // however they are being tokenized oddly.

    \{[^\{#%]+ {
        yypopState(); return handleContent();
    }
}

<hash> {
    "}" { yypopState(); return TwigTokenTypes.RBRACE; }
}

<expression, hash> {
    "(" { return TwigTokenTypes.LPARENTH; }
    ")" { return TwigTokenTypes.RPARENTH; }
    "[" { return TwigTokenTypes.LBRACKET; }
    "]" { return TwigTokenTypes.RBRACKET; }
    "{" { yypushState(hash); return TwigTokenTypes.LBRACE; }
    "}" { return TwigTokenTypes.RBRACE; }
    ":" { return TwigTokenTypes.COLON; }
    "true"/[}\)\t \n\x0B\f\r] { return TwigTokenTypes.BOOLEAN; }
    "false"/[}\)\t \n\x0B\f\r] { return TwigTokenTypes.BOOLEAN; }
    \-?[0-9]+(\.[0-9]+)?/[}\)\t \n\x0B\f\r] { return TwigTokenTypes.NUMBER; }
    "|" { return TwigTokenTypes.FILTER_SEP; }
    [\/.] { return TwigTokenTypes.SEP; }
    "=" { return TwigTokenTypes.EQUALS; }
    \"([^\"\\]|\\.)*\" { return TwigTokenTypes.STRING; }
    '([^'\\]|\\.)*' { return TwigTokenTypes.STRING; }

    "odd" |
    "even" {
        return TwigTokenTypes.TEST;
    }

    // in the order of operator precedence (except for 'not', which is a negator)
    "as" | "with" | "not" |
    "b-and" | "b-xor" | "b-or" |
    "or" | "and" |
    "==" | "!=" | "<" | ">" | ">=" | "<=" |
    "in" |
    "matches" |
    "starts with" | "ends with" |
    ".." |
    "+" | "-" | "~" | "*" | "/" | "//" | "%" | "is" | "**" |
    "??" | "?:" {
        return TwigTokenTypes.OPERATOR;
    }

    "," { return TwigTokenTypes.COMMA; }

    {Label} { return TwigTokenTypes.LABEL; }

    {WhiteSpace} { return TwigTokenTypes.WHITE_SPACE; }
}

<expression> {
    {StatementClose} {
        yypopState(); return TwigTokenTypes.STATEMENT_CLOSE;
    }

    {ExpressionClose} {
        yypopState(); return TwigTokenTypes.EXPRESSION_CLOSE;
    }
}

<comment> {
    {CommentClose} {
        yypopState(); return TwigTokenTypes.COMMENT_CLOSE;
    }

    ~{CommentClose} {
        // handle the extra - if it's a whitespace controlling tag
        if (yytext().subSequence(yylength() - 3, yylength() - 2).toString().equals("-")) {
            yypushback(1);
        }

        yypushback(2);
        return TwigTokenTypes.COMMENT_CONTENT;
    }

    // lex unclosed comments so that we can give better errors
    !([^]*"#}"[^]*) { return TwigTokenTypes.UNCLOSED_COMMENT; }
}

<statement_block_tag> {
    {Label} {
        yypopState();
        yypushState(expression);
        return TwigTokenTypes.TAG;
    }

    \~?{StatementClose} {
        yypopState();
        return TwigTokenTypes.STATEMENT_CLOSE;
    }
}

{WhiteSpace}+ { return TwigTokenTypes.WHITE_SPACE; }
. { return TwigTokenTypes.INVALID; }
