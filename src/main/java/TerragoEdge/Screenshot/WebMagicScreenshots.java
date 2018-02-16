package TerragoEdge.Screenshot;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

/**
 * Hello world!
 *
 */
public class WebMagicScreenshots {
	public static final String fileWithPath = "./EdgeScreenshots/";
	public static WebDriver webdriver = null;
	public static WebDriverWait driverWait;
	public static JavascriptExecutor jse;
	public static String url;
	public static String browser;
	public static String systemPath = System.getProperty("user.dir");
	public static Logger logger;
	public static Actions actions;
	public static String noteBookName;
	

	public static void main(String[] args) throws Exception 
	{
		File screenShotDir = new File(systemPath + "\\EdgeScreenshots\\");
		File logDir=new File(systemPath + "\\Files\\logs\\");
		FileUtils.cleanDirectory(screenShotDir);
		FileUtils.cleanDirectory(logDir);
		DOMConfigurator.configure(systemPath+"\\Files\\log4j.xml");
		logger = Logger.getLogger(WebMagicScreenshots.class.getName());

		url = args[0];
		browser = args[1];
		System.out.println(url + "/edgeServer/");
		if (webdriver == null) {
			if (browser.equalsIgnoreCase("chrome")) {
				System.setProperty("webdriver.chrome.driver", systemPath + "\\Files\\chromedriver.exe");
				webdriver = new ChromeDriver();
			} else {
				System.setProperty("webdriver.gecko.driver", systemPath + "\\Files\\geckodriver.exe");
				DesiredCapabilities capabilities = DesiredCapabilities.firefox();
				capabilities.setCapability("marionette", true);
				webdriver = new FirefoxDriver(capabilities);
			}
		}
		jse = (JavascriptExecutor) webdriver;
		actions = new Actions(webdriver);
		driverWait = new WebDriverWait(webdriver, 60);
		if(browser.equalsIgnoreCase("chrome"))
		{
			webdriver.manage().window().maximize();
		}
		webdriver.get(url + "/edgeServer/");
		checkPageIsReady();
	//	webdriver.manage().window().maximize();
		Thread.sleep(1500);
		logger.info("***************Started****************");
		takeSnapShot("Login");
		dashBoard();
		importScreen(); 
		  licenseScreen(); 
		  integrations();
		  locationService();
		  noteBook();
		  notes(); 
		  taskNotesScreen();
		  form(); 
		  mapView();
		  webdriver.close();
		  logger.info("***************END*************");
		 

	}

	public static void fileUpload(String fileName) throws InterruptedException, Exception {

		Robot robot = new Robot();
		robot.setAutoDelay(2000);
		// To Copy the url to clipboard
		String systemPath = System.getProperty("user.dir");
		StringSelection stringSelection = new StringSelection(systemPath + "\\Files\\" + fileName);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		robot.setAutoDelay(1000);
		// Control + V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		robot.setAutoDelay(1000);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}

	/* Get List Values Start */
	public static List<WebElement> webElementsByXpath(String locator) {
		return webdriver.findElements(By.xpath(locator));
	}
	/* Get List Values End */
	
	public static void takeSnapShotFullScreen(String fileName)
	{
		try {
			Screenshot fpScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(webdriver);
			   String fileDistinationPath = fileWithPath + fileName + ".png";
			   File DestFile = new File(fileDistinationPath);
			   ImageIO.write(fpScreenshot.getImage(),"PNG",DestFile);
			
			System.out.println("Screenshot has been taken");
			
		} catch (Exception e) {
			System.out.println("Failed to take screenshot");
			logger.info("Problem in screenshot method ");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void takeSnapShot(String fileName) {

		try {
			TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
			String fileDistinationPath = fileWithPath + fileName + ".png";
			File DestFile = new File(fileDistinationPath);
			FileUtils.copyFile(SrcFile, DestFile);
			
			System.out.println("Screenshot has been taken");
			
		} catch (Exception e) {
			System.out.println("Failed to take screenshot");
			logger.info("Problem in screenshot method ");
			e.printStackTrace();
			logger.error(e);
		}

	}

	public static void dashBoard() {
		homeScreen();
		recentNotes();
	}

	public static void homeScreen() {
		try {
			webdriver.findElement(By.id("inputUsername")).sendKeys("admin");
			webdriver.findElement(By.id("inputPassword1")).sendKeys("admin");
			webdriver
					.findElement(By.xpath("//button[@type='submit']"))
					
					.click();
			Thread.sleep(1500);
			webdriver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/ul[1]/div[1]/li[1]/a[1]/i[1]"))
					.click();
			takeSnapShot("HomeScreen");
			Thread.sleep(2500);
			logger.info("The screenshot has been taken for HomeScreen");
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for HomeScreen");
			logger.info("Failed to take a screenshot for HomeScreen");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void recentNotes() {
		try {
			webdriver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/ul[1]/div[1]/li[1]/a[1]/i[1]"))
					.click();
			Thread.sleep(2500);
			takeSnapShot("DashboardRecentNotes,TaskNotes,Notebooks,Maps,Forms");
			Thread.sleep(1000);
			logger.info("The screenshot has been taken for DashboardRecentNotes,TaskNotes,Notebooks,Maps,Forms");
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for DashboardRecentNotes,TaskNotes,Notebooks,Maps,Forms");
			logger.info("Failed to take a screenshot for DashboardRecentNotes,TaskNotes,Notebooks,Maps,Forms");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void importScreen() {
		importEdgeFiles();
		bulkNoteCreation();
		importShapefiles();
		importingKMLfiles();
		geoDatabase();
		geoPackage();
		arcGIS();
	}

	public static void importEdgeFiles() {
		try {
			webdriver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/ul[1]/div[1]/li[1]/a[1]/i[1]"))
					.click();
			webdriver.findElement(By.xpath("//*[@id='navSettingsDrop']/li[1]/a[1]")).click();
			Thread.sleep(1000);
			webdriver.findElement(By.xpath("//*[@id='importedgeTabs']/div[1]/div[1]/div[1]/div[1]/button[1]")).click();
			fileUpload("Sample Edge.edge");
			Thread.sleep(2500);
			takeSnapShot("ImportingEdgeFiles");
			logger.info("The screenshot has been taken for ImportingEdgeFiles");

		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for  ImportingEdgeFiles");
			logger.info("Failed to take a screenshot for ImportingEdgeFiles");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void bulkNoteCreation() {
		try {
			webdriver.findElement(By.xpath("//*[@id='importedgeTabs']/ul[1]/li[2]/a[1]")).click();
			Thread.sleep(1000);
			webdriver.findElement(By.xpath("//*[@id='importedgeTabs']/div[1]/div[2]/div[2]/div[1]/select[1]"))
					.sendKeys("image");
			webdriver.findElement(By.xpath("//*[@id='importedgeTabs']/div[1]/div[2]/div[3]/div[1]/select[1]"))
					.sendKeys("Test Notebook");
			webdriver.findElement(By.xpath("//*[@id='importedgeTabs']/div[1]/div[2]/div[1]/div[1]/button[1]")).click();
			fileUpload("images.jpg");
			Thread.sleep(2500);
			takeSnapShot("BulkNoteCreation");
			logger.info("The screenshot has been taken for BulkNoteCreation");
			

		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for BulkNoteCreation");
			logger.info("Failed to take a screenshot for BulkNoteCreation");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void importShapefiles() {
		try {
			webdriver.findElement(By.xpath("//*[@id='shapeFileTab']/a[1]")).click();
			Thread.sleep(2500);
			takeSnapShot("ImportingShapefiles");
			logger.info("The screenshot has been taken for ImportingShapefiles");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for ImportingShapefiles");
			logger.info("Failed to take a screenshot for ImportingShapefiles");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void importingKMLfiles() {
		try {
			webdriver.findElement(By.xpath("//*[@id='kmlTab']/a[1]")).click();
			Thread.sleep(2500);
			takeSnapShot("ImportingKMLfiles");
			logger.info("The screenshot has been taken for ImportingKMLfiles");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Importing KML files ");
			logger.info("Failed to take a screenshot for ImportingKMLfiles");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void geoDatabase() {
		try {
			webdriver.findElement(By.xpath("//*[@id='importedgeTabs']/ul[1]/li[5]/a[1]")).click();
			Thread.sleep(2500);
			takeSnapShot("ImportingGeodatabase");
			logger.info("The screenshot has been taken for ImportingGeodatabase");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Geodatabase ");
			logger.info("Failed to take a screenshot for ImportingGeodatabase");
			e.printStackTrace();
			logger.error(e);
		}

	}

	public static void geoPackage() {
		try {
			webdriver.findElement(By.xpath("//*[@id='importedgeTabs']/ul[1]/li[6]/a[1]")).click();
			Thread.sleep(2500);
			takeSnapShot("ImportingGeopackage");
			logger.info("The screenshot has been taken for ImportingGeopackage");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Geopackage ");
			logger.info("Failed to take a screenshot for ImportingGeopackage");
			e.printStackTrace();
			logger.error(e);
		}

	}

	public static void arcGIS() {
		try {
			webdriver.findElement(By.xpath("//*[@id='importedgeTabs']/ul[1]/li[7]/a[1]")).click();
			Thread.sleep(2500);
			takeSnapShot("ImportingArcGIS");
			webdriver.findElement(By.xpath("/html[1]/body[1]/div[10]/div[1]/div[1]/div[1]/div[1]/button[1]")).click();
			Thread.sleep(1000);
			logger.info("The screenshot has been taken for ImportingArcGIS");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for ArcGIS");
			logger.info("Failed to take a screenshot for ImportingArcGIS");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void noteBook() {
		creatingNotebook();
		addingMap();
		addingForms();
		enableQuickNote();
		addingUserAccess();
		notebookListView();
		notebookExport();
		notebookSelectMultiple();
	}

	public static void creatingNotebook() {
		try {
			webdriver.get(url + "/edgeServer/notebooks.jsp?view=list");
			Thread.sleep(2000);
			webdriver.findElement(By.xpath("//*[@id='mainContainer']/div[2]/div[1]/div[2]/a[1]/button[1]")).click();
			Thread.sleep(1000);
			webdriver.findElement(By.id("crNotebookname")).sendKeys("Test Notebook");
			webdriver.findElement(By.id("crNotebookdesc")).sendKeys("Test Notebook Details");
			Thread.sleep(2500);
			takeSnapShot("CreatingaNewNotebook");
			logger.info("The screenshot has been taken for Creating a New Notebook ");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for CreatingaNewNotebook");
			logger.info("Failed to take a screenshot for Creating a New Notebook");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void addingMap() {
		try {
			jse.executeScript("scroll(0,250)");
			Thread.sleep(2500);
			takeSnapShot("AddingMapsandWMSOverlaystoNotebook");
			logger.info("The screenshot has been taken for Adding Maps and WMS Overlays to Notebook");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for AddingMapsandWMSOverlaystoNotebook");
			logger.info("Failed to take a screenshot for Adding Maps and WMS Overlays to Notebook");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void addingForms() {
		try {
			webdriver.findElement(By.xpath("//*[@id='tabs']/li[3]/a[1]")).click();
			Thread.sleep(1000);
			webdriver.findElement(By.xpath("//li[contains(.,'Agriculture') ]/input[@type='checkbox']")).click();
			Thread.sleep(2500);
			takeSnapShot("AddingFormTemplatestoNotebook");
			logger.info("The screenshot has been taken for Adding FormTemplates to Notebook");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for AddingFormTemplatestoNotebook");
			logger.info("Failed to take a screenshot for Adding FormTemplates to Notebook");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void enableQuickNote() {
		try {
			webdriver.findElement(By.xpath("//*[@id='note-FormTemplate']/div[1]/div[1]/div[1]/div[1]/button[1]")).click();
			// new code for quicknote dropdown
			Select chooseQuickForm = new Select(webdriver.findElement(By.xpath("//select[@ng-model='getnoteFormName']")));
			chooseQuickForm.selectByVisibleText("Harvest Results");
			Select chooseNoteType = new Select(webdriver.findElement(By.xpath("//select[@ng-model='getnoteName']")));
			chooseNoteType.selectByVisibleText("Default name");
			// old code
			/*
			 * webdriver.findElement(By.xpath(
			 * "//*[@id='note-FormTemplate']/div[1]/div[1]/div[1]/div[3]/div[2]/select[1]"
			 * )).sendKeys("Harvest Results"); webdriver.findElement(By.xpath(
			 * "//*[@id='note-FormTemplate']/div[1]/div[1]/div[1]/div[3]/div[3]/div[3]/select[1]"
			 * )).sendKeys("Default name");
			 */
			Thread.sleep(2500);
			takeSnapShot("EnablingQuickNoteForm");
			logger.info("The screenshot has been taken for Enabling QuickNote Form");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for EnablingQuickNoteForm");
			logger.info("Failed to take a screenshot for Enabling QuickNote Form");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void addingUserAccess() {
		try {
			webdriver.findElement(By.xpath("//*[@id='tabs']/li[4]/a[1]/span[1]")).click();
			Thread.sleep(2500);
			takeSnapShot("AddingUserAccesstoNotebook");
			jse.executeScript("scroll(250,0)");
			webdriver.findElement(By.id("book-submit")).click();
			noteBookName=webdriver.findElement(By.id("crNotebookname")).getAttribute("value");
			checkNbExist();
			logger.info("The screenshot has been taken for AddingUserAccesstoNotebook");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for AddingUserAccesstoNotebook");
			logger.info("Failed to take a screenshot for AddingUserAccesstoNotebook");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void checkNbExist() {
		try {
			WebElement nbAlert = webdriver.findElement(By.xpath("//span[@ng-if='noteBookMessage']"));
			nbAlert.isDisplayed();
			Random random = new Random();
			int randomNum = random.nextInt(1000);
			webdriver.findElement(By.id("crNotebookname")).clear();
			webdriver.findElement(By.id("crNotebookname")).sendKeys("Test Notebook" + randomNum);
			noteBookName=webdriver.findElement(By.id("crNotebookname")).getAttribute("value");
			webdriver.findElement(By.id("book-submit")).click();
			
		} catch (Exception e) {
			System.out.println("Nb doesnt exist");
			
		}
	}

	public static void notebookListView() {
		try {
			// webdriver.get(url+"/edgeServer/notebooks.jsp?view=list");
			Thread.sleep(2000);
					driverWait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[@id='notebooksList']/table[1]/tbody[1]/tr[1]/td[1]/div[1]/button[1]/a[1]/i[1]")));
					Thread.sleep(2500);
			takeSnapShot("NotebooksListView");
			logger.info("The screenshot has been taken for NotebooksListView");
		
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for NotebooksListView");
			logger.info("Failed to take a screenshot for NotebooksListView");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void notebookExport() {
		try {

			driverWait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[@id='notebooksList']/table[1]/tbody[1]/tr[1]/td[1]/div[1]/button[1]/a[1]/i[1]")));
			webdriver
					.findElement(By
							.xpath("//*[@id='notebooksList']/table[1]/tbody[1]/tr[1]/td[1]/div[1]/button[1]/a[1]/i[1]"))
					.click();
			webdriver
					.findElement(
							By.xpath("//*[@id='notebooksList']/table[1]/tbody[1]/tr[1]/td[1]/div[1]/ul[1]/li[4]/a[1]"))
					.click();
			Thread.sleep(2500);
			takeSnapShot("NotebookExport");
			logger.info("The screenshot has been taken for NotebookExport");
		
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for NotebookExport");
			logger.info("Failed to take a screenshot for NotebookExport");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void notebookSelectMultiple() {
		try {

			webdriver.findElement(By.xpath("//*[@id='exportModal']/div[1]/div[1]/div[1]/button[1]")).click();
			Thread.sleep(1000);
			webdriver.findElement(By.xpath("//*[@id='selectmul']/a[1]")).click();
			Thread.sleep(2500);
			takeSnapShot("NotebooksSelectMultiple");
			logger.info("The screenshot has been taken for Notebooks Select Multiple");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for NotebooksSelectMultiple");
			logger.info("Failed to take a screenshot for Notebooks Select Multiple");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void notes() throws Exception {
		creatingNote();
		resourceType();
		photoAttach();
		videoAttach();
		audioAttach();
		fileAttach();
		multipleResource();
		formAttach();
		blankAndCloned();
		notesListView();
		notesSelectMultiple();
		notesMapView();
	}

	public static void creatingNote() {
		try {
			webdriver.get(url + "/edgeServer/notebooks.jsp?view=list");
			Thread.sleep(2000);
			/* Select a Notebook from list start */
			List<WebElement> notebookList = webElementsByXpath("//*[@id='notebooksList']/table/tbody//td[4]/a");
			for (WebElement expectedNbName : notebookList) {
				System.out.println(expectedNbName.getText());
				if (expectedNbName.getText() != null && expectedNbName.getText().equals("Test Notebook")) {
					webdriver.findElement(By.xpath("//td[contains(@ng-click,'showNotes')]/a[text()='Test Notebook']"))
							.click();
					Thread.sleep(2000);
					break;
				} else {
					System.out.println("Notebook Not Found");
				}
			}
			/* Select a Notebook from list end */
			Thread.sleep(2000);
			//takeSnapShot("Creating a Note");
			logger.info("The screenshot has been taken for Creating a Note ");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Creating a Note ");
			logger.info("Failed to take a screenshot for Creating a Note ");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void resourceType() {
		try {
			webdriver.findElement(By.xpath("//button[@ng-show='checkifNotebookNote']")).click();
			jse.executeScript("window.scrollBy(0,650)", "");
			Thread.sleep(2500);
			takeSnapShotFullScreen("ResourceTypesinaNote");
			logger.info("The screenshot has been taken for Resource Types in a Note");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Resource Types in a Note ");
			logger.info("Failed to take a screenshot for Resource Types in a Note");
			
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void photoAttach() {
		try {
			webdriver
					.findElement(By
							.xpath("//*[@id='mainContainer']/div[1]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/a[1]/span[1]/i[1]"))
					.click();
			fileUpload("images.jpg");
			webdriver.findElement(By.xpath("//*[@id='mainContainer']/div[1]/div[2]/div[1]/div[1]/div[2]/div[1]/div[2]/table[1]/tbody[1]/tr[1]/td[1]/a[1]")).click();
			Thread.sleep(2500);
			takeSnapShot("PhotoattachedinaNote");
			logger.info("The screenshot has been taken for Photo attached in a note ");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Photo attached in a note");
			logger.info("Failed to take a screenshot for Photo attached in a note");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void videoAttach() {
		try {
			webdriver.findElement(By.xpath("//*[@id='imagepopup']/div[1]/div[1]/div[1]/button[1]")).click();
			Thread.sleep(1000);
			webdriver
					.findElement(By
							.xpath("//*[@id='mainContainer']/div[1]/div[2]/div[1]/div[1]/div[2]/div[1]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a[1]"))
					.click();
			webdriver
					.findElement(By
							.xpath("//*[@id='mainContainer']/div[1]/div[2]/div[1]/div[1]/div[1]/div[2]/div[2]/a[1]/span[1]/i[1]"))
					.click();
			fileUpload("SampleVideo.mp4");
			Thread.sleep(2000);
			takeSnapShotFullScreen("VideoattachedinaNote");
			logger.info("The screenshot has been taken for  Video attached in a note");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Video attached in a note ");
			logger.info("Failed to take a screenshot for  Video attached in a note");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void audioAttach() {
		try {
			webdriver
					.findElement(By
							.xpath("//*[@id='mainContainer']/div[1]/div[2]/div[1]/div[1]/div[2]/div[1]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a[1]"))
					.click();
			webdriver
					.findElement(By
							.xpath("//*[@id='mainContainer']/div[1]/div[2]/div[1]/div[1]/div[1]/div[2]/div[3]/a[1]/span[1]/i[1]"))
					.click();
			fileUpload("SampleAudio.mp3");
			Thread.sleep(2500);
			takeSnapShotFullScreen("AudioattachedinaNote");
			logger.info("The screenshot has been taken for Audio attached in a note ");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Audio attached in a note");
			logger.info("Failed to take a screenshot for Audio attached in a note ");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void fileAttach() {
		try {
			webdriver
					.findElement(By
							.xpath("//*[@id='mainContainer']/div[1]/div[2]/div[1]/div[1]/div[2]/div[1]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a[1]"))
					.click();
			webdriver
					.findElement(By
							.xpath("//*[@id='mainContainer']/div[1]/div[2]/div[1]/div[1]/div[1]/div[2]/div[6]/a[1]/span[1]/i[1]"))
					.click();
			fileUpload("Sample File.pdf");
			Thread.sleep(2500);
			takeSnapShotFullScreen("FileattachmentsinaNote");
			logger.info("The screenshot has been taken for  File attachments");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for File attachments ");
			logger.info("Failed to take a screenshot for  File attachments");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void multipleResource() {
		try {
			webdriver
					.findElement(By
							.xpath("//*[@id='mainContainer']/div[1]/div[2]/div[1]/div[1]/div[2]/div[1]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a[1]"))
					.click();
			webdriver
					.findElement(By
							.xpath("//*[@id='mainContainer']/div[1]/div[2]/div[1]/div[1]/div[1]/div[2]/div[7]/a[1]/span[1]/i[1]"))
					.click();
			Thread.sleep(1000);
			webdriver
					.findElement(By
							.xpath("//*[@id='createtask']/div[14]/div[1]/div[1]/div[2]/form[1]/ul[1]/li[1]/div[2]/div[1]/a[1]"))
					.click();
			fileUpload("images.jpg");
			webdriver
					.findElement(By
							.xpath("//*[@id='createtask']/div[14]/div[1]/div[1]/div[2]/form[1]/ul[1]/li[2]/div[2]/div[1]/a[1]"))
					.click();
			fileUpload("SampleVideo.mp4");
			webdriver
					.findElement(By
							.xpath("//*[@id='createtask']/div[14]/div[1]/div[1]/div[2]/form[1]/ul[1]/li[3]/div[2]/div[1]/a[1]"))
					.click();
			fileUpload("SampleAudio.mp3");
			Thread.sleep(2500);
			takeSnapShot("MultipleResourcesinaNote");
			logger.info("The screenshot has been taken for Multiple Resources in a Note");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Multiple Resources in a Note ");
			logger.info("Failed to take a screenshot for Multiple Resources in a Note");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void formAttach() {
		try {
			webdriver.findElement(By.xpath("//*[@id='createtask']/div[14]/div[1]/div[1]/div[1]/button[1]")).click();
			Thread.sleep(1000);
			webdriver
					.findElement(By
							.xpath("//*[@id='mainContainer']/div[1]/div[2]/div[1]/div[1]/div[1]/div[2]/div[5]/a[1]/span[1]/i[1]"))
					.click();
			Thread.sleep(1000);
			webdriver.findElement(By.xpath("//div[contains(.,'Farm Survey') ]/input[@type='radio']")).click();
			webdriver.findElement(By.xpath("//*[@id='selectform']")).click();
			Thread.sleep(1000);
			webdriver
					.findElement(By
							.xpath("//*[@id='createtask']/div[14]/div[1]/div[1]/div[2]/form[1]/ul[1]/li[2]/div[2]/div[2]/input[1]"))
					.click();
			jse.executeScript("scroll(0,500)");
			webdriver
					.findElement(
							By.xpath("//*[@id='createtask']/div[14]/div[1]/div[1]/div[2]/form[1]/div[1]/button[3]"))
					.click();
			Thread.sleep(2500);
			takeSnapShotFullScreen("FormattachedinaNote");
			logger.info("The screenshot has been taken for Form attached in note");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Form attached in note ");
			logger.info("Failed to take a screenshot for Form attached in note ");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void blankAndCloned() {
		try {
			Thread.sleep(1000);
			jse.executeScript("scroll(250,0)");
			webdriver.findElement(By.id("sdf")).sendKeys("Test Form");
			webdriver.findElement(By.id("note-submit")).click();
			Thread.sleep(2000);

			/* Select a Notes from list start */
			List<WebElement> notesList = webElementsByXpath("//*[@id='notesListView']/table/tbody//td[4]/a");
			for (WebElement expectedNoteName : notesList) {
				System.out.println(expectedNoteName.getText());

				if (expectedNoteName.getText() != null && expectedNoteName.getText().equals("Test Form")) {
					webdriver.findElement(By.xpath("//a[text()='Test Form']")).click();
					Thread.sleep(2000);
					break;
				} else {
					System.out.println("Notebook Not Found");
				}
			}
			/* Select a Notes from list end */

			webdriver.findElement(By.xpath("//*[@id='mainContainer']/div[1]/div[1]/div[2]/div[1]/button[1]")).click();
			//webdriver.findElement(By.xpath("//*[@id='mainContainer']/div[1]/div[1]/div[2]/div[1]/ul[1]/li[2]/a[1]")).click();
			Thread.sleep(2500);
			takeSnapShot("CreatingBlankandClonedNotes");
			logger.info("The screenshot has been taken for Creating Blank and Cloned Notes");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Creating Blank and Cloned Notes ");
			logger.info("Failed to take a screenshot for Creating Blank and Cloned Notes");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void notesListView() {
		try {
			webdriver.get(url + "/edgeServer/notesListing.jsp?notesIndex");
			Thread.sleep(2500);
			takeSnapShot("NotesListView");
			logger.info("The screenshot has been taken for Notes - List View");
		
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Notes - List View");
			logger.info("Failed to take a screenshot for Notes - List View");
			e.printStackTrace();
			logger.error(e);
		}
		
	}

	public static void notesSelectMultiple() {
		try {
			driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='selectmul']/a[1]")));
			webdriver.findElement(By.xpath("//*[@id='selectmul']/a[1]")).click();
			Thread.sleep(2500);
			takeSnapShot("NotesSelectMultiple");
			logger.info("The screenshot has been taken for Notes - Select Multiple ");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Notes - Select Multiple ");
			logger.info("Failed to take a screenshot for Notes - Select Multiple");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void notesMapView() {
		try {
			driverWait.until(
					ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='notesIconRight']/a[3]/i[1]")));
			webdriver.findElement(By.xpath("//*[@id='notesIconRight']/a[3]/i[1]")).click();
			Thread.sleep(2500);
			takeSnapShot("NotesMapView");
			logger.info("The screenshot has been taken for Notes - Map View");
		
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Notes - Map View");
			logger.info("Failed to take a screenshot for Notes - Map View");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void offlineMaps() throws InterruptedException {
		mapListView();
		mapSelectMultiple();
		mapMultipleNotebooks();
	}

	public static void mapListView() {
		try {
			webdriver.get(url + "/edgeServer/basemaps.jsp");
			// webdriver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/ul[1]/li[10]/a[1]")).click();
			driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[@ng-show='homemaps']")));
			Thread.sleep(2500);
			takeSnapShot("MapsListView");
			logger.info("The screenshot has been taken for Maps List View");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Maps List View ");
			logger.info("Failed to take a screenshot for Maps List View ");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void mapSelectMultiple() {
		try {
			webdriver.findElement(By.id("selectmul")).click();
			driverWait.until(ExpectedConditions.elementToBeClickable(By.id("chkbxMultiSelectAll_ListView")));
			Thread.sleep(2500);
			takeSnapShot("MapsSelectMultiple");
			webdriver.findElement(By.xpath("//*[@id='cancelshow']/a")).click();
			logger.info("The screenshot has been taken for Maps – Select Multiple");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Maps – Select Multiple ");
			logger.info("Failed to take a screenshot for Maps – Select Multiple");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void mapMultipleNotebooks() {
		try {
			driverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='mapsListView']/table[1]/tbody[1]/tr[1]/td[1]/div[1]/button[1]")));
			webdriver.findElement(By.xpath("//*[@id='mapsListView']/table[1]/tbody[1]/tr[1]/td[1]/div[1]/button[1]")).click();
			webdriver.findElement(By.xpath("(//ul[@class='dropdown-menu'])/li[1]/a[@ng-click='attachMapsFormWindow(map.assigned,map.basemapGuid)']")).click();
			Thread.sleep(2500);
			takeSnapShot("AttachingamaptoMultipleNotebooks");
			jse.executeScript("scroll(250,0)");
			Thread.sleep(3000);
			webdriver.findElement(By.xpath("(//BUTTON[@type='button'][text()='Cancel'][text()='Cancel'])[2]")).click();
			logger.info("The screenshot has been taken for Attach map to multiple notebooks");
		
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Attach map to multiple notebooks ");
			logger.info("Failed to take a screenshot for Attach map to multiple notebooks");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void geometryList() {
		notesPoint();
		notesPolyline();
	}

	public static void notesPoint() {
		try {
			webdriver.get(url + "/edgeServer/dashboard.jsp");
			webdriver.findElement(By.xpath("//a[@class='btn btn-xs btn-primary']")).click();
			// point
			
			webdriver.findElement(By.xpath("//span[@ng-click='note_markertool()']/img")).click();
			webdriver.findElement(By.id("noteSearchLocation")).sendKeys("Canada");
			driverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='map']/div[1]/div[1]/div[1]/div[2]/img[1]")));
			webdriver.findElement(By.xpath("//*[@id='map']/div[1]/div[1]/div[1]/div[2]/img[1]")).click();
			webdriver.findElement(By.xpath("//span[@ng-click='note_done()']")).click();

			webdriver.findElement(By.xpath("//ul[@class='nav nav-tabs notePropertiesNav']/li[2]/a")).click();
			webdriver.findElement(By.xpath("//input[@ng-model='note.locationDescription']")).sendKeys("Canada");

			Thread.sleep(2500);
			takeSnapShot("MarkyourlocationPoint");
			logger.info("The screenshot has been taken for Mark your location - Point");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Mark your location - Point ");
			logger.info("Failed to take a screenshot for Mark your location - Point");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void notesPolyline() {
		try {
			
			webdriver.findElement(By.xpath("//span[@ng-click='Mappolylinetools()']/img")).click();
			driverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[7]/div/div[2]/div[2]/span/div/div[3]/div/div[1]/div[1]/div/div[2]/img[3]")));
			WebElement fromVertex = webdriver.findElement(By
					.xpath("/html/body/div[7]/div/div[2]/div[2]/span/div/div[3]/div/div[1]/div[1]/div/div[2]/img[3]"));
			Thread.sleep(1500);
			int width = fromVertex.getSize().getWidth();
			actions.moveToElement(fromVertex).moveByOffset((width / 2) - 2, 0).click().perform();
			// jse.executeScript("scroll(0,150)");
			WebElement toVertex = webdriver.findElement(By
					.xpath("/html/body/div[7]/div/div[2]/div[2]/span/div/div[3]/div/div[1]/div[1]/div/div[2]/img[3]"));
			Thread.sleep(1500);

			actions.moveToElement(toVertex).moveByOffset((width / 2), -150).doubleClick().build().perform();
			// for chrome
			actions.moveToElement(toVertex).moveByOffset((width / 2), -150).doubleClick().build().perform();
			// actions.doubleClick(toVertex).build().perform();
			Thread.sleep(1500);
			webdriver.findElement(By.xpath("//span[@ng-click='note_done()']")).click();
			jse.executeScript("scroll(0,150)");
			Thread.sleep(2500);
			takeSnapShot("MarkyourlocationPolylineandPolygon");
			logger.info("The screenshot has been taken for Mark your location – Polyline");
		
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Mark your location – Polyline ");
			logger.info("Failed to take a screenshot for Mark your location – Polyline");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void licenseScreen() {
		addNewUser();
		addUserToMobile();
		addDevice();
		ModifyUserandDeviceSettings();
		GroupAndDeviceSettings();
	}

	public static void addNewUser() {
		try {
			webdriver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/ul[1]/div[1]/li[1]/a[1]/i[1]"))
					.click();
			webdriver.findElement(By.xpath("//*[@id='navSettingsDrop']/li[3]/a[1]")).click();
			Thread.sleep(1000);
			//takeSnapShot("Figure 12 Add New User");
			logger.info("The screenshot has been taken for Add New User");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Add New User");
			logger.info("Failed to take a screenshot for Add New User");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void addUserToMobile() {
		try {

			Random num = new Random();
			int randomNumber = num.nextInt(1000);
			webdriver.findElement(By.xpath("//*[@ng-model='adminCtrl.userName']")).sendKeys("NewTest" + randomNumber);
			webdriver.findElement(By.id("tt")).sendKeys("terragoedge@gmail.com");
			webdriver.findElement(By.xpath("//*[@ng-model='adminCtrl.password']")).sendKeys("password");
			webdriver.findElement(By.xpath("//*[@ng-model='adminCtrl.confirmPassword']")).sendKeys("password");
			webdriver.findElement(By.id("enableMobile")).click();
			webdriver.findElement(By.xpath("//*[@ng-click='checkFunction()']")).click();
			Thread.sleep(1000);
			driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@ng-click='deviceEdit.addDevice()']")));
			webdriver.findElement(By.xpath("//*[@ng-click='deviceEdit.addDevice()']")).click();
			Thread.sleep(2500);
			takeSnapShotFullScreen("ManageUser,GroupandDeviceSettings");
			logger.info("The screenshot has been taken for Add device to user");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for  Add device to user");
			logger.info("Failed to take a screenshot for Add device to user");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void addDevice() {
		try {
			webdriver
					.findElement(By
							.xpath("//*[@id='createadm']/div[12]/div[1]/div[1]/div[2]/div[2]/div[1]/table[1]/tbody[1]/tr[1]/td[4]/input[1]"))
					.sendKeys("Test");
			//takeSnapShot("Figure16 Add device");
			logger.info("The screenshot has been taken for Add device");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Add device");
			logger.info("Failed to take a screenshot for Add device");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void ModifyUserandDeviceSettings() {
		try {
			webdriver.findElement(By.xpath("//*[@ng-click='deviceEdit.cancel()']")).click();
			Thread.sleep(1000);
			jse.executeScript("window.scrollBy(0,250)", "");
			Thread.sleep(1000);
			webdriver.findElement(By.id("dropdownMenu1")).click();
			Thread.sleep(2500);
			takeSnapShot("ManageUser,GroupandDeviceSettings");
			logger.info("The screenshot has been taken for Modify User and Device Settings");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Modify User and Device Settings ");
			logger.info("Failed to take a screenshot for Modify User and Device Settings");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void GroupAndDeviceSettings() {
		try {
			jse.executeScript("scroll(250,0)");
			webdriver.findElement(By.xpath("//*[@id='management']/li[2]/a[1]")).click();
			//takeSnapShot("Group and Device Settings");
			logger.info("The screenshot has been taken for Group and Device Settings");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Group and Device Settings");
			logger.info("Failed to take a screenshot for Group and Device Settings ");
			e.printStackTrace();
			logger.error(e);
		}

	}

	public static void integrations() throws InterruptedException {
		try {
			webdriver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/ul[1]/div[1]/li[1]/a[1]/i[1]"))
					.click();
			webdriver.findElement(By.xpath("//*[@id='navSettingsDrop']/li[6]/a[1]")).click();
			Thread.sleep(2500);
			takeSnapShot("ArcGIS Integration");
			logger.info("The screenshot has been taken for ArcGIS Integration ");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for ArcGIS Integration");
			logger.info("Failed to take a screenshot for ArcGIS Integration");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void locationService() throws InterruptedException {
		measurementTypeStatic();
		userLocator();
		measurementTypeDynamic();

	}

	public static void measurementTypeStatic() {
		try {
			webdriver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/ul[1]/div[1]/li[1]/a[1]/i[1]"))
					.click();
			webdriver.findElement(By.xpath("//*[@id='navSettingsDrop']/li[7]/a[1]")).click();
			Thread.sleep(2500);
			takeSnapShot("LocationServicesConfiguration");
			jse.executeScript("window.scrollBy(0,250)", "");
			//takeSnapShot("Figure22 Measurement type Static");
			Thread.sleep(1000);
			logger.info("The screenshot has been taken for Measurement type Static");
		
		} catch (Exception e) {
			System.out.println("Failed to take a Measurement type Static");
			logger.info("Failed to take a screenshot for Measurement type Static");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void userLocator() {
		try {
			webdriver
					.findElement(By
							.xpath("//*[@id='mainContainer']/div[1]/div[3]/div[1]/table[1]/tbody[1]/tr[1]/td[1]/div[1]/button[2]"))
					.click();
			// webdriver.findElement(By.xpath("//*[@id='mainContainer']/div[1]/div[3]/div[1]/table[1]/tbody[1]/tr[1]/td[1]/form[1]/div[1]/select[1]")).click();
			Thread.sleep(2000);
			//takeSnapShot("Figure21 User Locator");
			Thread.sleep(1000);
			logger.info("The screenshot has been taken for User Locator");
		
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for User Locator");
			logger.info("Failed to take a screenshot for User Locator");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void measurementTypeDynamic() {
		try {

			webdriver
					.findElement(By
							.xpath("//*[@id='mainContainer']/div[1]/div[4]/div[1]/table[1]/tbody[1]/tr[1]/td[1]/div[1]/div[1]/select[1]"))
					.click();
			Thread.sleep(2000);
			//takeSnapShot("Figure23 Measurement type - Dynamic");
			jse.executeScript("scroll(250,0)");
			Thread.sleep(1000);
			// webdriver.findElement(By.xpath("//*[@id='content']/div[1]/div[1]/div[1]/ul[1]/li[2]/a[1]")).click();
			webdriver.get(url + "/edgeServer/dashboard.jsp");
			logger.info("The screenshot has been taken for Measurement type - Dynamic");
			;
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Measurement type - Dynamic");
			logger.info("Failed to take a screenshot for Measurement type - Dynamic");
			e.printStackTrace();
			logger.error(e);
		}

	}

	public static void taskNotesScreen() {
		CreatingATasknote();
		taskNotesListView();
		taskNotesSelectMultiple();
	}

	public static void CreatingATasknote() {
		try {
			webdriver.get(url + "/edgeServer/notesListing.jsp?notesIndex");
			Thread.sleep(2000);
			webdriver.findElement(By.xpath("/html/body/div[7]/div[2]/div/div[3]/a[2]/button[1]")).click();
			webdriver.findElement(By.id("sdf")).sendKeys("Test Task");
			webdriver.findElement(By.xpath("/html/body/div[7]/div/div[2]/div[1]/ul/li[3]/a")).click();
			Thread.sleep(2500);
			takeSnapShot("CreatingaTasknote");
			logger.info("The screenshot has been taken for Creating a Tasknote ");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Creating a Tasknote ");
			logger.info("Failed to take a screenshot for Creating a Tasknote ");
			e.printStackTrace();
			logger.error(e);
		}

	}

	public static void taskNotesListView() {
		try {
			webdriver.findElement(By.id("note-submit")).click();
			Thread.sleep(1500);
			webdriver.get(url + "/edgeServer/notesListing.jsp?taskNotes");
			Thread.sleep(2500);
			takeSnapShot("TasknotesListView");
			logger.info("The screenshot has been taken for Tasknotes - List View");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for ");
			logger.info("Failed to take a screenshot for Tasknotes - List View");
			e.printStackTrace();
			logger.error(e);
		}

	}

	public static void taskNotesSelectMultiple() {
		try {
			webdriver.findElement(By.xpath("//*[@id='selectmul']/a[1]")).click();
			Thread.sleep(2500);
			takeSnapShot("TasknotesSelectMultiple");
			logger.info("The screenshot has been taken for Tasknotes - Select Multiple");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for TasknotesSelectMultiple");
			logger.info("Failed to take a screenshot for Tasknotes - Select Multiple ");
			e.printStackTrace();
			logger.error(e);
		}

	}

	public static void form() throws Exception {
		formsListView();
		attach_Forms_MultipleNb();
		formsSelectMultiple();
		creatingNewForm();
		importingFormTemplate();
		FormPropertiesAndConditions();
	}

	public static void formsListView() {
		try {
			webdriver.get(url + "/edgeServer/formsView.jsp");
			Thread.sleep(2500);
			takeSnapShot("FormsListView");
			logger.info("The screenshot has been taken for Forms - List View");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Forms - List View");
			logger.info("Failed to take a screenshot for Forms - List View");
			e.printStackTrace();
			logger.error(e);
		}

	}

	public static void attach_Forms_MultipleNb() {
		try {
			webdriver
					.findElement(By
							.xpath("//*[@id='formsDataShowInListView']/table[1]/tbody[1]/tr[1]/td[1]/div[1]/button[1]/a[1]/i[1]"))
					.click();
			webdriver
					.findElement(By
							.xpath("//*[@id='formsDataShowInListView']/table[1]/tbody[1]/tr[1]/td[1]/div[1]/ul[1]/li[2]/a[1]"))
					.click();
			Thread.sleep(2500);
			takeSnapShot("Attachingaformtomultiplenotebooks");
			logger.info("The screenshot has been taken for Attaching a form to multiple notebooks ");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Attaching a form to multiple notebooks");
			logger.info("Failed to take a screenshot for Attaching a form to multiple notebooks");
			e.printStackTrace();
			logger.error(e);
		}

	}

	public static void formsSelectMultiple() {
		try {
			webdriver.findElement(By.xpath("//*[@id='attachModal']/div[1]/div[1]/div[3]/button[1]")).click();
			Thread.sleep(1000);
			webdriver.findElement(By.xpath("//*[@id='selectmul']/a[1]")).click();
			Thread.sleep(2500);
			takeSnapShot("FormsSelectMultiple");
			logger.info("The screenshot has been taken for Forms – Select Multiple");
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Forms – Select Multiple ");
			logger.info("Failed to take a screenshot for Forms – Select Multiple");
			e.printStackTrace();
			logger.error(e);
		}

	}

	public static void creatingNewForm() {
		try {
			webdriver.get(url + "/edgeServer/formsView.jsp");
			Thread.sleep(2000);
			webdriver.findElement(By.xpath("//*[@id='mainContainer']/div[4]/div[1]/div[2]/a[2]/button[1]")).click();
			Thread.sleep(2500);
			takeSnapShot("CreatingaNewForm");
			logger.info("The screenshot has been taken for Creating a New Form");
		
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for CreatingaNewForm ");
			logger.info("Failed to take a screenshot for Creating a New Form");
			e.printStackTrace();
			logger.error(e);
		}

	}

	public static void importingFormTemplate() {
		try {
			webdriver.get(url + "/edgeServer/formsView.jsp");
			Thread.sleep(2000);
			webdriver.findElement(By.xpath("//*[@id='mainContainer']/div[4]/div[1]/div[2]/a[1]")).click();
			Thread.sleep(1000);
			webdriver.findElement(By.xpath("//*[@id='importFormTemplate']/div[1]/div[1]/div[2]/div[1]/div[1]/button[1]")).click();
			fileUpload("Sample Form Template.json");
			Thread.sleep(2500);
			takeSnapShot("ImportingaFormTemplate");
			logger.info("The screenshot has been taken for Importing a Form Template");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Importing a Form Template");
			logger.info("Failed to take a screenshot for  Importing a Form Template");
			
			e.printStackTrace();
			logger.error(e);
		}

	}

	public static void FormPropertiesAndConditions() {
		try {
			webdriver.findElement(By.xpath("//*[@id='importFormTemplate']/div[1]/div[1]/div[3]/button[1]")).click();
			Thread.sleep(1000);
			webdriver.findElement(By.xpath("//li[@class='search_filter']/input")).click();
			webdriver.findElement(By.xpath("//li[@class='search_filter']/input")).sendKeys("Pipeline Inspection Survey");
			driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='formsDataShowInListView']/table[1]/tbody[1]/tr[1]/td[1]/div[1]/button[1]/a[1]/i[1]")));
			webdriver.findElement(By.xpath("//*[@id='formsDataShowInListView']/table[1]/tbody[1]/tr[1]/td[1]/div[1]/button[1]/a[1]/i[1]")).click();
			
			webdriver.findElement(By.xpath("//*[@id='formsDataShowInListView']/table[1]/tbody[1]/tr[1]/td[1]/div[1]/ul[1]/li[1]/a[1]")).click();
			driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='main']/form-preview[1]/div[1]/div[1]/form[1]/ul[1]/li[2]/form-component[1]/div[2]/div[2]/div[1]/label[1]")));
			actions.moveToElement(webdriver.findElement(By.xpath("//*[@id='main']/form-preview[1]/div[1]/div[1]/form[1]/ul[1]/li[2]/form-component[1]/div[2]/div[2]/div[1]/label[1]"))).perform();
			driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='main']/form-preview[1]/div[1]/div[1]/form[1]/ul[1]/li[2]/form-component[1]/div[1]/form-settings-control[1]/div[1]/i[1]")));
			webdriver.findElement(By.xpath("//*[@id='main']/form-preview[1]/div[1]/div[1]/form[1]/ul[1]/li[2]/form-component[1]/div[1]/form-settings-control[1]/div[1]/i[1]")).click();
			Thread.sleep(2500);
			takeSnapShot("SettingFormElementPropertiesandConditions");
			logger.info("The screenshot has been taken for Setting Form ElementPropertiesand Conditions");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Setting Form Element Properties and Conditions");
			logger.info("Failed to take a screenshot for Setting Form Element Properties and Conditions");
			e.printStackTrace();
			logger.error(e);
		}

	}

	public static void mapView() throws InterruptedException, AWTException {
		mapCreation();
		offlineMaps();
		overLays();
		importMapFiles();
		geometryList();
	}

	/* Chandru Code Start */

	public static void mapCreation() {

		createNewMap();
		selectBaseMap();
		searchLocation();
		drawMapArea();

		/*
		 * webdriver.get(url+"/edgeServer/basemaps.jsp");
		 * takeSnapShot("Figure 29 Create a New Map");
		 * webdriver.findElement(By.xpath(
		 * "//*[contains(@ng-click,'edgemap.jsp?')]")).click();
		 * webdriver.findElement(By.id("mapnameenter")).sendKeys("Houston");
		 * 
		 * webdriver.findElement(By.id("edgeChangeMaps")).click();
		 * Thread.sleep(1500); takeSnapShot("Figure 30 Select base map");
		 * Thread.sleep(500);
		 * 
		 * webdriver.findElement(By.id("mapsearchlocation")).
		 * sendKeys("Houston Harris County TX US");
		 * 
		 * jse.
		 * executeScript("document.getElementById('mapsearchlocation').value='Houston Harris County TX US';"
		 * ); Thread.sleep(2000);
		 * 
		 * takeSnapShot("Figure 31 Search Location");
		 * 
		 * webdriver.findElement(By.id("select-map-area")).click(); WebElement
		 * mapAreaFrom = webdriver.findElement(By.xpath(
		 * "//*[@src='http://b.tile.stamen.com/terrain/12/1166/1565.png']"));
		 * WebElement mapAreaTo = webdriver.findElement(By.xpath(
		 * "//*[@src='http://b.tile.stamen.com/terrain/12/1167/1564.png']"));
		 * 
		 * Actions actions = new Actions(webdriver);
		 * actions.clickAndHold(mapAreaFrom).build().perform();
		 * Thread.sleep(2000);
		 * actions.moveToElement(mapAreaTo).click().build().perform();
		 * takeSnapShot("Figure 32 Draw Map Area");
		 * 
		 * jse.executeScript("scroll(-250,0)");
		 * webdriver.findElement(By.id("mapsave_button")).click();
		 * takeSnapShot("Figure 33 Save the Map"); Thread.sleep(2000);
		 * driverWait.until(ExpectedConditions.elementToBeClickable(
		 * webdriver.findElement(By.xpath(
		 * "//*[contains(@ng-click,'mapprogressalert()')]"))));
		 * webdriver.findElement(By.xpath(
		 * "//*[contains(@ng-click,'mapprogressalert()')]")).click();
		 * takeSnapShot("Figure 34 Map Creation Progress");
		 */

	}

	public static void createNewMap() {
		try {
			webdriver.get(url + "/edgeServer/basemaps.jsp");
			webdriver.findElement(By.xpath("//*[contains(@ng-click,'edgemap.jsp?')]")).click();
			webdriver.findElement(By.id("mapnameenter")).sendKeys("Houston");
			Thread.sleep(2500);
			takeSnapShot("CreatingaNewMap");
			logger.info("The screenshot has been taken for Create a New Map ");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Create a new map");
			logger.info("Failed to take a screenshot for Create a New Map ");
			logger.error(e);
		}
	}

	public static void selectBaseMap() {
		try {
			webdriver.findElement(By.id("edgeChangeMaps")).click();
			Thread.sleep(1500);
			//takeSnapShot("Figure 30 Select base map");
			Thread.sleep(500);
			logger.info("The screenshot has been taken for Select base map");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Select base map");
			logger.info("Failed to take a screenshot for Select base map");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void searchLocation() {
		try {
			webdriver.findElement(By.id("mapsearchlocation")).sendKeys("Houston Harris County TX US");

			jse.executeScript("document.getElementById('mapsearchlocation').value='Houston Harris County TX US';");
			Thread.sleep(2000);

			//takeSnapShot("Figure 31 Search Location");
			logger.info("The screenshot has been taken for Search Location");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Search Location");
			logger.info("Failed to take a screenshot for Search Location ");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void drawMapArea() {
		try {
			webdriver.findElement(By.id("select-map-area")).click();
			driverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='leaflet-tile-container leaflet-zoom-animated']/img[3]")));
			WebElement mapAreaFrom = webdriver
					.findElement(By.xpath("//div[@class='leaflet-tile-container leaflet-zoom-animated']/img[3]"));
			driverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='leaflet-tile-container leaflet-zoom-animated']/img[4]")));
			WebElement mapAreaTo = webdriver
					.findElement(By.xpath("//div[@class='leaflet-tile-container leaflet-zoom-animated']/img[4]"));
			
			actions.clickAndHold(mapAreaFrom).build().perform();
			Thread.sleep(2000);
			actions.moveToElement(mapAreaTo).click().build().perform();
			Thread.sleep(2500);
			//takeSnapShot("Figure 32 Draw Map Area");
			logger.info("The screenshot has been taken for Draw Map Area ");
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Draw Map Area");
			logger.info("Failed to take a screenshot for Draw Map Area");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void overLays() {
		mapOverlays();
		mapOverlaysSelectMultiple();
		creatingNewMapOverlay();

	}

	public static void mapOverlays() {
		try {
			webdriver.get(url + "/edgeServer/basemaps.jsp");
			webdriver.findElement(By.id("mapoverlaya")).click();
			driverWait.until(ExpectedConditions.presenceOfElementLocated(By
					.xpath("//*[@id='overlaymapsListView']/table[1]/tbody[1]/tr[1]/td[1]/div[1]/button[1]/a[1]/i[1]")));
			webdriver
					.findElement(By
							.xpath("//*[@id='overlaymapsListView']/table[1]/tbody[1]/tr[1]/td[1]/div[1]/button[1]/a[1]/i[1]"))
					.click();
			Thread.sleep(2500);
			takeSnapShot("MapOverlaysListView");
			logger.info("The screenshot has been taken for Map Overlays ");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Map Overlays ");
			logger.info("Failed to take a screenshot for Map Overlays ");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void mapOverlaysSelectMultiple() {
		try {
			webdriver.findElement(By.id("wmsselectmul")).click();
			driverWait.until(ExpectedConditions.presenceOfElementLocated(By
					.xpath("//*[@id='overlaymapsListView']/table[1]/tbody[1]/tr[1]/td[1]/div[1]/button[1]/a[1]/i[1]")));
			Thread.sleep(2500);
			takeSnapShot("MapOverlaysSelectMultiple");
			logger.info("The screenshot has been taken for Map Overlays – Select Multiple");
		
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Map Overlays – Select Multiple");
			logger.info("Failed to take a screenshot for Map Overlays – Select Multiple");
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void creatingNewMapOverlay() {
		try {
			webdriver.findElement(By.xpath("//*[@id='wmscancelshow']/a")).click();
			webdriver.findElement(By.xpath("//a[@ng-click='createOverlay()']/button")).click();
			webdriver.findElement(By.xpath("//*[@ng-model='wmsurl']"))
					.sendKeys("https://basemap.nationalmap.gov/arcgis/services/USGSImageryOnly/MapServer/WMSServer");
			webdriver.findElement(By.xpath("//span[@class='input-group-btn']")).click();
			driverWait.until(ExpectedConditions
					.invisibilityOfElementLocated(By.xpath("//*[@id='overlay-content']/div[2]/div[2]/div[1]/div[1]")));
			Select select = new Select(webdriver.findElement(By.id("wmslayerselection")));
			select.selectByValue("0");
			webdriver.findElement(By.xpath("//*[@class='btn btn-default dropdown-toggle']")).click();
			Thread.sleep(2500);
			takeSnapShot("CreatingaNewMapOverlay");
			logger.info("The screenshot has been taken for Creating a New Map Overlay ");
			
		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for Creating a New Map Overlay ");
			logger.info("Failed to take a screenshot for Creating a New Map Overlay ");
			e.printStackTrace();
			logger.error(e);
		}

	}
	
	/*public static void importMapFiles() throws InterruptedException {
		try {
			System.out.println("//li[@ng-class=\"{active: navBar.location == '" + "maps" + "'}\"]/i");
			webdriver.get(url + "/edgeServer/basemaps.jsp");
			// webdriver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/ul[1]/li[10]/a[1]")).click();
			driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@data-toggle='modal']")));
			webdriver.findElement(By.xpath("//span[@data-toggle='modal']")).click();
			Thread.sleep(1500);
			driverWait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//div[@ng-model='filesImport']/span")));
			//click select
			WebElement selectFile=webdriver.findElement(By.xpath("//div[@ng-model='filesImport']/span"));
			int width = selectFile.getSize().getWidth();
			actions.moveToElement(selectFile).moveByOffset((width / 2) , 0).click().perform();
			try
			{
			fileUpload("offlinemap.pdf");
			
			driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@ng-show='mapUploadCount']/label")));
			Select nbDropdown=new Select(webdriver.findElement(By.xpath("//select[@ng-model='pdffileNotebook']")));
			nbDropdown.selectByVisibleText("Test Notebook");
			driverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='btn btn-primary basemapImportButton']")));
			webdriver.findElement(By.xpath("//button[@class='btn btn-primary basemapImportButton']")).click();
			}catch(UnhandledAlertException e)
			{
				webdriver.switchTo().alert().accept();
				System.out.println("Alert is handled");
				driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@ng-show='mapUploadCount']/label")));
				Select nbDropdown=new Select(webdriver.findElement(By.xpath("//select[@ng-model='pdffileNotebook']")));
				nbDropdown.selectByVisibleText("Test Notebook");
				driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='btn btn-primary basemapImportButton']")));
				webdriver.findElement(By.xpath("//button[@class='btn btn-primary basemapImportButton']")).click();
			}
			
		
			//dp adjustment
			driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='rz-pointer rz-pointer-min']")));
			WebElement dpAdjustment=webdriver.findElement(By.xpath("//*[@class='rz-pointer rz-pointer-min']"));
			for(int i=1;i<=4;i++)
			{
			actions.keyDown(dpAdjustment, Keys.ARROW_LEFT);
			}
			webdriver.findElement(By.id("entireArea")).click();
			webdriver.findElement(By.id("mapsave_button")).click();
			driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='dashsuccessalert']")));
			webdriver.findElement(By.xpath("//*[@ng-click='closemapsuccessalert()']")).click();
			takeSnapShot("Figure 37 Multiple map file import");
			logger.info("The screenshot has been taken for Multiple map file import ");

		} catch (Exception e) {
			System.out.println("Failed to take a screenshot for importMapFiles ");
			logger.info("Failed to take a screenshot for importMapFiles");
			e.printStackTrace();
			logger.error(e);
		}
	}*/
	
	public static void importMapFiles() throws InterruptedException {
		  try {
		   System.out.println("//li[@ng-class=\"{active: navBar.location == '" + "maps" + "'}\"]/i");
		   webdriver.get(url + "/edgeServer/basemaps.jsp");
		   // webdriver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/ul[1]/li[10]/a[1]")).click();
		   driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@data-toggle='modal']")));
		   webdriver.findElement(By.xpath("//span[@data-toggle='modal']")).click();
		   Thread.sleep(1500);
		   driverWait.until(ExpectedConditions.elementToBeClickable(
		     By.xpath("//div[@ng-model='filesImport']/span")));
		   //click select
		   WebElement selectFile=webdriver.findElement(By.xpath("//div[@ng-model='filesImport']/span"));
		   int width = selectFile.getSize().getWidth();
		   actions.moveToElement(selectFile).moveByOffset((width / 2) , 0).click().perform();
		   try
		   {
		   fileUpload("offlinemap.pdf");
		   
		   driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@ng-show='mapUploadCount']/label")));
		   Select nbDropdown=new Select(webdriver.findElement(By.xpath("//select[@ng-model='pdffileNotebook']")));
		   nbDropdown.selectByVisibleText("Test Notebook");
		   driverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='btn btn-primary basemapImportButton']")));
		   webdriver.findElement(By.xpath("//button[@class='btn btn-primary basemapImportButton']")).click();
		   }catch(UnhandledAlertException e)
		   {
		    webdriver.switchTo().alert().accept();
		    System.out.println("Alert is handled");
		    driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@ng-show='mapUploadCount']/label")));
		    Select nbDropdown=new Select(webdriver.findElement(By.xpath("//select[@ng-model='pdffileNotebook']")));
		    nbDropdown.selectByVisibleText("Test Notebook");
		    driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='btn btn-primary basemapImportButton']")));
		    webdriver.findElement(By.xpath("//button[@class='btn btn-primary basemapImportButton']")).click();
		   }

		   //dp adjustment
		   driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='rz-pointer rz-pointer-min']")));
		   WebElement dpAdjustment=webdriver.findElement(By.xpath("//*[@class='rz-pointer rz-pointer-min']"));
		   Robot robot=new Robot();
		   dpAdjustment.click();
		    
		    Dimension sliderSize = dpAdjustment.getSize();
		    int sliderWidth = sliderSize.getWidth();
		    
		    int xCoord = dpAdjustment.getLocation().getX();
		        
		    Actions builder = new Actions(webdriver);   
		    builder.moveToElement(dpAdjustment)
		        .click()
		        .dragAndDropBy
		          (dpAdjustment,-xCoord + sliderWidth, 2)
		        .build()
		        .perform();
		 
		   webdriver.findElement(By.id("entireArea")).click();
		   webdriver.findElement(By.id("mapsave_button")).click();
		   driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='dashsuccessalert']")));
		   webdriver.findElement(By.xpath("//*[@ng-click='closemapsuccessalert()']")).click();
		   Thread.sleep(2500);
		   takeSnapShot("Importmap");
		   logger.info("The screenshot has been taken for Multiple map file import");

		  } catch (Exception e) {
		   System.out.println("Failed to take a screenshot for importMapFiles ");
		   logger.info("Failed to take a screenshot for importMapFiles");
		   e.printStackTrace();
		   logger.error(e);
		  }
		 }
	
	public void testShouldBeAbleToOverrideTheWindowAlertMethod() {
	    jse.executeScript(
	        "window.alert = function(msg) { document.getElementById('text').innerHTML = msg; }");
	    webdriver.findElement(By.id("alert")).click();
	  }

	/* Chandru Code End */

	public static void checkPageIsReady() {
		JavascriptExecutor js = (JavascriptExecutor) webdriver;

		// Initially bellow given if condition will check ready state of page.
		if (js.executeScript("return document.readyState").toString().equals("complete")) {
			System.out.println("Page Is loaded.");
			return;
		}

		// This loop will rotate for 25 times to check If page Is ready after
		// every 1 second.
		// You can replace your value with 25 If you wants to Increase or
		// decrease wait time.
		for (int i = 0; i < 25; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			// To check page ready state.
			if (js.executeScript("return document.readyState").toString().equals("complete")) {
				break;
			}
		}
	}

}
