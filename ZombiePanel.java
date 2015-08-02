/*
 * Copyright 2015 Henry Elliott
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ZombiePanel extends JPanel {
	ZombieMath win;
	JSlider foodSlider;
	JSlider waterSlider;
	JSlider bulletsSlider;
	JLabel foodReport;
	JLabel waterReport;
	JLabel bulletsReport;
	JLabel survivalReport;
	Cabin cabin;

	public ZombiePanel(ZombieMath window) {
		win = window;
		setLayout(new GridLayout(5, 1));

		JPanel foodPanel = getPanel();
		foodPanel.add(getLabel("Percent of Time Spent Gathering Food."));
		foodSlider = getSlider();
		foodPanel.add(foodSlider);

		JPanel waterPanel = getPanel();
		waterPanel.add(getLabel("Percent of Time Spent Stockpiling Water."));
		waterSlider = getSlider();
		waterPanel.add(waterSlider);

		JPanel bulletsPanel = getPanel();
		bulletsPanel.add(getLabel("Percent of Time Spent Making Bullets."));
		bulletsSlider = getSlider();
		bulletsPanel.add(bulletsSlider);

		JPanel buttonPanel = new JPanel();
		JButton results = new JButton("See Results");
		results.addActionListener(new ResultListener());
		buttonPanel.add(results);

		JPanel reportPanel = getPanel();
		foodReport = getLabel("");
		reportPanel.add(foodReport);
		waterReport = getLabel("");
		reportPanel.add(waterReport);
		bulletsReport = getLabel("");
		reportPanel.add(bulletsReport);
		survivalReport = getLabel("");
		reportPanel.add(survivalReport);

		add(foodPanel);
		add(waterPanel);
		add(bulletsPanel);
		add(buttonPanel);
		add(reportPanel);
	}

	private static JSlider getSlider() {
		JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		slider.setMajorTickSpacing(2);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		return slider;
	}

	private static JPanel getPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		return panel;
	}

	private static JLabel getLabel(String text) {
		JLabel label = new JLabel(text);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		return label;
	}

	private class ResultListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				cabin = new Cabin(foodSlider.getValue(),
						  waterSlider.getValue(), bulletsSlider.getValue());
				foodReport.setText(cabin.foodStatus());
				waterReport.setText(cabin.waterStatus());
				bulletsReport.setText(cabin.bulletsStatus());
				survivalReport.setText(cabin.survivalStatus());
				win.repaint();
			} catch (IllegalArgumentException ex) {
				foodReport.setText("");
				waterReport.setText("");
				bulletsReport.setText("");
				survivalReport.setText("");
				JOptionPane.showMessageDialog(win, ex.getMessage(),
				        "Invalid Input", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
}
