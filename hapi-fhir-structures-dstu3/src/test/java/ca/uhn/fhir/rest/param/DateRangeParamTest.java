package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.testing.EqualsTester;
import org.junit.AfterClass;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static ca.uhn.fhir.rest.param.ParamPrefixEnum.*;
import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DateRangeParamTest {

	private static final SimpleDateFormat ourFmt;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DateRangeParamTest.class);
	private static FhirContext ourCtx = FhirContext.forDstu3();

	static {
		ourFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Test
	public void testGetLowerRange() {
		ourLog.info("Time is {}", new Date());

		DateRangeParam param = new DateRangeParam(new DateParam("2011-01-02"));
		ourLog.info("Adjusted time is " + param.getLowerBoundAsInstant().toString());
	}

	private static DateRangeParam create(String theLower, String theUpper) throws InvalidRequestException {
		DateRangeParam p = new DateRangeParam();
		List<QualifiedParamList> tokens = new ArrayList<QualifiedParamList>();
		tokens.add(QualifiedParamList.singleton(null, theLower));
		if (theUpper != null) {
			tokens.add(QualifiedParamList.singleton(null, theUpper));
		}
		p.setValuesAsQueryTokens(ourCtx, null, tokens);
		return p;
	}

	public static Date parse(String theString) throws ParseException {
		return ourFmt.parse(theString);
	}

	public static Date parseM1(String theString) throws ParseException {
		return new Date(ourFmt.parse(theString).getTime() - 1L);
	}

	private DateRangeParam create(String theString) {
		return new DateRangeParam(new DateParam(theString));
	}

	@Test
	public void testAddAnd() {
		assertEquals(1, new DateAndListParam().addAnd(new DateOrListParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new NumberAndListParam().addAnd(new NumberOrListParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new ReferenceAndListParam().addAnd(new ReferenceOrListParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new QuantityAndListParam().addAnd(new QuantityOrListParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new UriAndListParam().addAnd(new UriOrListParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new StringAndListParam().addAnd(new StringOrListParam()).getValuesAsQueryTokens().size());
	}

	@Test
	public void testAndList() {
		assertNotNull(new DateAndListParam().newInstance());
		assertNotNull(new NumberAndListParam().newInstance());
		assertNotNull(new ReferenceAndListParam().newInstance());
		assertNotNull(new QuantityAndListParam().newInstance());
		assertNotNull(new UriAndListParam().newInstance());
		assertNotNull(new StringAndListParam().newInstance());
	}

	@Test
	public void testAndOr() {
		assertEquals(1, new DateOrListParam().addOr(new DateParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new NumberOrListParam().addOr(new NumberParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new ReferenceOrListParam().addOr(new ReferenceParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new QuantityOrListParam().addOr(new QuantityParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new UriOrListParam().addOr(new UriParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new StringOrListParam().addOr(new StringParam()).getValuesAsQueryTokens().size());
	}

	@Test
	public void testDay() throws Exception {
		assertEquals(parse("2011-01-01 00:00:00.0000"), create(">=2011-01-01", "<2011-01-02").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-02 00:00:00.0000"), create(">=2011-01-01", "<2011-01-02").getUpperBoundAsInstant());
		assertEquals(parse("2011-01-02 00:00:00.0000"), create(">2011-01-01", "<=2011-01-02").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-03 00:00:00.0000"), create(">2011-01-01", "<=2011-01-02").getUpperBoundAsInstant());

		assertEquals(parse("2011-01-01 00:00:00.0000"), create("ge2011-01-01", "lt2011-01-02").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-02 00:00:00.0000"), create("ge2011-01-01", "lt2011-01-02").getUpperBoundAsInstant());
		assertEquals(parse("2011-01-02 00:00:00.0000"), create("gt2011-01-01", "le2011-01-02").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-03 00:00:00.0000"), create("gt2011-01-01", "le2011-01-02").getUpperBoundAsInstant());
	}

	@Test
	public void testFromQualifiedDateParam() throws Exception {
		assertEquals(parse("2011-01-01 00:00:00.0000"), create("2011-01-01").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-02 00:00:00.0000"), create("2011-01-01").getUpperBoundAsInstant());

		assertEquals(parse("2011-01-01 00:00:00.0000"), create("ge2011-01-01").getLowerBoundAsInstant());
		assertEquals(null, create("ge2011-01-01").getUpperBoundAsInstant());

		assertEquals(null, create("le2011-01-01").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-02 00:00:00.0000"), create("le2011-01-01").getUpperBoundAsInstant());
	}

	@Test
	public void testMonth() throws Exception {
		assertEquals(parse("2011-01-01 00:00:00.0000"), create("ge2011-01", "lt2011-02").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-02-01 00:00:00.0000"), create("ge2011-01", "lt2011-02").getUpperBoundAsInstant());

		assertEquals(parse("2011-02-01 00:00:00.0000"), create("gt2011-01", "le2011-02").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-03-01 00:00:00.0000"), create("gt2011-01", "le2011-02").getUpperBoundAsInstant());
	}

	@Test
	public void testOnlyOneParam() throws Exception {
		assertEquals(parse("2011-01-01 00:00:00.0000"), create("2011-01-01").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-02 00:00:00.0000"), create("2011-01-01").getUpperBoundAsInstant());
	}

	@Test
	public void testSetBoundsWithDatesInclusive() {
		DateRangeParam range = new DateRangeParam();
		range.setLowerBoundInclusive(new Date());
		range.setUpperBoundInclusive(new Date());

		assertEquals(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, range.getLowerBound().getPrefix());
		assertEquals(ParamPrefixEnum.LESSTHAN_OR_EQUALS, range.getUpperBound().getPrefix());
	}

	@Test
	public void testSetBoundsWithDatesExclusive() {
		DateRangeParam range = new DateRangeParam();
		range.setLowerBoundExclusive(new Date());
		range.setUpperBoundExclusive(new Date());

		assertEquals(ParamPrefixEnum.GREATERTHAN, range.getLowerBound().getPrefix());
		assertEquals(ParamPrefixEnum.LESSTHAN, range.getUpperBound().getPrefix());
	}

	@Test
	public void testOrList() {
		assertNotNull(new DateOrListParam().newInstance());
		assertNotNull(new NumberOrListParam().newInstance());
		assertNotNull(new ReferenceOrListParam().newInstance());
		assertNotNull(new QuantityOrListParam().newInstance());
		assertNotNull(new UriOrListParam().newInstance());
		assertNotNull(new StringOrListParam().newInstance());
	}

	@Test
	public void testRange() {
		InstantDt start = new InstantDt("2015-09-23T07:43:34.811-04:00");
		InstantDt end = new InstantDt("2015-09-23T07:43:34.899-04:00");
		DateParam lowerBound = new DateParam(ParamPrefixEnum.GREATERTHAN, start.getValue());
		DateParam upperBound = new DateParam(ParamPrefixEnum.LESSTHAN, end.getValue());
		assertEquals(ParamPrefixEnum.GREATERTHAN, lowerBound.getPrefix());
		assertEquals(ParamPrefixEnum.LESSTHAN, upperBound.getPrefix());

		/*
		 * When DateParam (which extends DateTimeDt) gets passed in, make sure we preserve the comparators..
		 */
		DateRangeParam param = new DateRangeParam(lowerBound, upperBound);
		ourLog.info(param.toString());
		assertEquals(ParamPrefixEnum.GREATERTHAN, param.getLowerBound().getPrefix());
		assertEquals(ParamPrefixEnum.LESSTHAN, param.getUpperBound().getPrefix());

		param = new DateRangeParam(new DateTimeDt(lowerBound.getValue()), new DateTimeDt(upperBound.getValue()));
		ourLog.info(param.toString());
		assertEquals(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, param.getLowerBound().getPrefix());
		assertEquals(ParamPrefixEnum.LESSTHAN_OR_EQUALS, param.getUpperBound().getPrefix());

	}

	@Test
	public void testRangeFromDates() {
		TimeZone tz = TimeZone.getDefault();
		TimeZone.setDefault(TimeZone.getTimeZone("America/Toronto"));
		try {
			Date startDate = new InstantDt("2010-01-01T00:00:00.000Z").getValue();
			Date endDate = new InstantDt("2010-01-01T00:00:00.001Z").getValue();
			DateTimeDt startDateTime = new DateTimeDt(startDate, TemporalPrecisionEnum.MILLI);
			DateTimeDt endDateTime = new DateTimeDt(endDate, TemporalPrecisionEnum.MILLI);

			DateRangeParam range = new DateRangeParam(startDateTime, endDateTime);
			assertEquals("2009-12-31T19:00:00.000-05:00", range.getValuesAsQueryTokens().get(0).getValueAsString());
			assertEquals("2009-12-31T19:00:00.001-05:00", range.getValuesAsQueryTokens().get(1).getValueAsString());

			// Now try with arguments reversed (should still create same range)
			range = new DateRangeParam(endDateTime, startDateTime);
			assertEquals("2009-12-31T19:00:00.000-05:00", range.getValuesAsQueryTokens().get(0).getValueAsString());
			assertEquals("2009-12-31T19:00:00.001-05:00", range.getValuesAsQueryTokens().get(1).getValueAsString());

		} finally {
			TimeZone.setDefault(tz);
		}
	}

	@Test
	public void testSecond() throws Exception {
		assertEquals(parse("2011-01-01 00:00:00.0000"), create("ge2011-01-01T00:00:00", "lt2011-01-01T01:00:00").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-01 02:00:00.0000"), create("ge2011-01-01T00:00:00", "lt2011-01-01T02:00:00").getUpperBoundAsInstant());

		assertEquals(parse("2011-01-01 00:00:01.0000"), create("gt2011-01-01T00:00:00", "le2011-01-01T02:00:00").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-01 02:00:01.0000"), create("gt2011-01-01T00:00:00", "le2011-01-01T02:00:00").getUpperBoundAsInstant());
	}

	@Test
	public void testYear() throws Exception {
		assertEquals(parse("2011-01-01 00:00:00.0000"), create("ge2011", "lt2012").getLowerBoundAsInstant());
		assertEquals(parseM1("2012-01-01 00:00:00.0000"), create("ge2011", "lt2012").getUpperBoundAsInstant());

		assertEquals(parse("2012-01-01 00:00:00.0000"), create("gt2011", "le2012").getLowerBoundAsInstant());
		assertEquals(parseM1("2014-01-01 00:00:00.0000"), create("gt2011", "le2013").getUpperBoundAsInstant());
	}

	@Test()
	public void testEqualsAndHashCode() {
		Date lowerBound = new Date(currentTimeMillis());
		Date upperBound = new Date(lowerBound.getTime() + SECONDS.toMillis(1));
		new EqualsTester()
			.addEqualityGroup(new DateRangeParam(),
				new DateRangeParam((Date) null, (Date) null))
			.addEqualityGroup(new DateRangeParam(lowerBound, upperBound),
				new DateRangeParam(new DateParam(GREATERTHAN_OR_EQUALS, lowerBound), new DateParam(LESSTHAN_OR_EQUALS, upperBound)))
			.addEqualityGroup(new DateRangeParam(new DateParam(EQUAL, lowerBound)),
				new DateRangeParam(new DateParam(null, lowerBound)),
				new DateRangeParam(new DateParam(EQUAL, lowerBound), new DateParam(EQUAL, lowerBound)))
			.addEqualityGroup(new DateRangeParam(lowerBound, null),
				new DateRangeParam(new DateParam(GREATERTHAN_OR_EQUALS, lowerBound), null))
			.addEqualityGroup(new DateRangeParam(null, upperBound),
				new DateRangeParam(null, new DateParam(LESSTHAN_OR_EQUALS, upperBound)))
			.testEquals();
	}
}
