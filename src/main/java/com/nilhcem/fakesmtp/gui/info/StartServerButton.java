package com.nilhcem.fakesmtp.gui.info;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import com.nilhcem.fakesmtp.core.exception.BindPortException;
import com.nilhcem.fakesmtp.core.exception.InvalidPortException;
import com.nilhcem.fakesmtp.core.exception.OutOfRangePortException;
import com.nilhcem.fakesmtp.model.UIModel;

/**
 * Button to start / stop the SMTP server.
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class StartServerButton extends Observable {
	private static final String START_SERVER_STR = "Start server";
	private static final String STOP_SERVER_STR = "Stop server";

	private final JButton button = new JButton(StartServerButton.START_SERVER_STR);

	/**
	 * Creates a start / stop button to start and stop the SMTP server.
	 * <p>
	 * If the user selects a wrong port before starting the server, the method will display an error message.
	 * </p>
	 */
	public StartServerButton() {
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					UIModel.INSTANCE.toggleButton();
					toggleButton();
				} catch (InvalidPortException ipe) {
					displayError(String.format("Can't start SMTP Server.%n\"Listening port\" is invalid."));
				} catch (BindPortException bpe) {
					displayError(String.format("Can't start SMTP Server.%nMake sure no other program is listening on port %d.", bpe.getPort()));
				} catch (OutOfRangePortException orpe) {
					displayError(String.format("Can't start SMTP Server.%nPort %d is out of range.", orpe.getPort()));
				} catch (RuntimeException re) {
					displayError(String.format("Error starting SMTP Server:%n%s.", re.getMessage()));
				}
			}
		});
	}

	/**
	 * Switches the text inside the button and calls the PortTextField observer to enable/disable the port field.
	 *
	 * @see PortTextField
	 */
	private void toggleButton() {
		String btnText;
		if (UIModel.INSTANCE.isStarted()) {
			btnText = StartServerButton.STOP_SERVER_STR;
		} else {
			btnText = StartServerButton.START_SERVER_STR;
		}
		button.setText(btnText);
		setChanged();
		notifyObservers();
	}

	/**
	 * Returns the JButton object.
	 *
	 * @return the JButton object.
	 */
	public JButton get() {
		return button;
	}

	/**
	 * Displays a message dialog displaying the error specified in parameter.
	 *
	 * @param error a string representing the error which will be displayed in a message dialog.
	 */
	private void displayError(String error) {
		JOptionPane.showMessageDialog(button.getParent(), error, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
