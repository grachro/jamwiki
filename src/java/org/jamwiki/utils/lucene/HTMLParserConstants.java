/* Generated By:JavaCC: Do not edit this line. HTMLParserConstants.java */
// package com.tchibo.misc.lucene;
package org.jamwiki.utils.lucene;

/**
 *
 */
public interface HTMLParserConstants {

  /**
   * TODO: Document this field.
   */
  int EOF = 0;
  /**
   * TODO: Document this field.
   */
  int TagName = 1;
  /**
   * TODO: Document this field.
   */
  int DeclName = 2;
  /**
   * TODO: Document this field.
   */
  int Comment1 = 3;
  /**
   * TODO: Document this field.
   */
  int Comment2 = 4;
  /**
   * TODO: Document this field.
   */
  int Word = 5;
  /**
   * TODO: Document this field.
   */
  int LET = 6;
  /**
   * TODO: Document this field.
   */
  int NUM = 7;
  /**
   * TODO: Document this field.
   */
  int Entity = 8;
  /**
   * TODO: Document this field.
   */
  int Space = 9;
  /**
   * TODO: Document this field.
   */
  int SP = 10;
  /**
   * TODO: Document this field.
   */
  int Punct = 11;
  /**
   * TODO: Document this field.
   */
  int ArgName = 12;
  /**
   * TODO: Document this field.
   */
  int ArgEquals = 13;
  /**
   * TODO: Document this field.
   */
  int TagEnd = 14;
  /**
   * TODO: Document this field.
   */
  int ArgValue = 15;
  /**
   * TODO: Document this field.
   */
  int ArgQuote1 = 16;
  /**
   * TODO: Document this field.
   */
  int ArgQuote2 = 17;
  /**
   * TODO: Document this field.
   */
  int Quote1Text = 19;
  /**
   * TODO: Document this field.
   */
  int CloseQuote1 = 20;
  /**
   * TODO: Document this field.
   */
  int Quote2Text = 21;
  /**
   * TODO: Document this field.
   */
  int CloseQuote2 = 22;
  /**
   * TODO: Document this field.
   */
  int CommentText1 = 23;
  /**
   * TODO: Document this field.
   */
  int CommentEnd1 = 24;
  /**
   * TODO: Document this field.
   */
  int CommentText2 = 25;
  /**
   * TODO: Document this field.
   */
  int CommentEnd2 = 26;

  /**
   * TODO: Document this field.
   */
  int DEFAULT = 0;
  /**
   * TODO: Document this field.
   */
  int WithinTag = 1;
  /**
   * TODO: Document this field.
   */
  int AfterEquals = 2;
  /**
   * TODO: Document this field.
   */
  int WithinQuote1 = 3;
  /**
   * TODO: Document this field.
   */
  int WithinQuote2 = 4;
  /**
   * TODO: Document this field.
   */
  int WithinComment1 = 5;
  /**
   * TODO: Document this field.
   */
  int WithinComment2 = 6;

  /**
   * TODO: Document this field.
   */
  String[] tokenImage = {
	"<EOF>",
	"<TagName>",
	"<DeclName>",
	"\"<!--\"",
	"\"<!\"",
	"<Word>",
	"<LET>",
	"<NUM>",
	"<Entity>",
	"<Space>",
	"<SP>",
	"<Punct>",
	"<ArgName>",
	"\"=\"",
	"<TagEnd>",
	"<ArgValue>",
	"\"\\\'\"",
	"\"\\\"\"",
	"<token of kind 18>",
	"<Quote1Text>",
	"<CloseQuote1>",
	"<Quote2Text>",
	"<CloseQuote2>",
	"<CommentText1>",
	"\"-->\"",
	"<CommentText2>",
	"\">\"",
  };

}
