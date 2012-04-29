package myline.shared;

public class Utils {
	  public  String getCustomStackTrace(Throwable aThrowable) {
		    //add the class name and any message passed to constructor
		    final StringBuilder result = new StringBuilder();
		    result.append(aThrowable.toString());
		    final String NEW_LINE = "\n";
		    result.append(NEW_LINE);

		    //add each element of the stack trace
		    if(aThrowable.getStackTrace() != null){
			    for (StackTraceElement element : aThrowable.getStackTrace() ){
			      result.append( element );
			      result.append( NEW_LINE );
			    }
		    }
		    return result.toString();
		  }
}
