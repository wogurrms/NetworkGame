// This class takes in an array of images.
// Calling getImage() gives you the appropriate
// image in the animation cycle.
//이 클래스는 이미지 배열을 가져온다
//getImage()를 부르면 너에게 애니메이션 사이클에 적절한 이미지를 제공할거다

package com.neet.DiamondHunter.Entity;

import java.awt.image.BufferedImage;

public class Animation {
	
	private BufferedImage[] frames;
	private int currentFrame;
	private int numFrames;
	
	private int count;
	private int delay;
	
	private int timesPlayed;
	
	public Animation() {
		timesPlayed = 0;
	}
	
	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
		currentFrame = 0;
		count = 0;
		timesPlayed = 0;
		delay = 2;
		numFrames = frames.length;
	}
	
	public void setDelay(int i) { delay = i; }
	public void setFrame(int i) { currentFrame = i; }
	public void setNumFrames(int i) { numFrames = i; }
	
	public void update() {
		
		if(delay == -1) return;
		
		count++;
		
		if(count == delay) {
			currentFrame++;
			count = 0;
		}
		if(currentFrame == numFrames) {
			currentFrame = 0;
			timesPlayed++;
		}
		
	}
	
	public int getFrame() { return currentFrame; }
	public int getCount() { return count; }
	public BufferedImage getImage() { return frames[currentFrame]; }
	public boolean hasPlayedOnce() { return timesPlayed > 0; }
	public boolean hasPlayed(int i) { return timesPlayed == i; }
	
}