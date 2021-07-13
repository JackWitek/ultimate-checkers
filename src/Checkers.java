/*////////////////////////////////////////////////////////////
//
//	              Ultimate Checkers Version 9
//
//	        Group 24                April 12th 2014
//
//           Mykhaylo Dubinets: 1138447
//              Katherine Joun: 0867396
//                Ian Phillips: 0964538
//               Mohdeep Singh: 0963824
//             James Singleton: 0952057
//                  Jack Witek: 1211058
//
////////////////////////////////////////////////////////////*/

import windows.StartWindow;

// This class holds the main method
public class Checkers {

	// This is the main method that creates the initial start window
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				StartWindow.createAndShowStartWindow();
			}
		});
	}

}
