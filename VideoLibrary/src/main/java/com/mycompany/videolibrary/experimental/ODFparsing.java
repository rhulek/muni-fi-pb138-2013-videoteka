package com.mycompany.videolibrary.experimental;

import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.draw.Textbox;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;
import org.odftoolkit.simple.text.Paragraph;
import org.odftoolkit.simple.text.Section;

public class ODFparsing {


	public static void main(String[] args) {
		
		try {
			TextDocument doc = TextDocument.loadDocument("DemoTemplate.odt");
			Table roomtable = doc.getTableByName("RoomTable");
			roomtable.remove();
			Section templateSection = doc.getSectionByName("SectionForm");
			
			SpreadsheetDocument data = SpreadsheetDocument.loadDocument("Passengers.ods");
			Table table = data.getTableByName("passenger");
			int count = table.getRowCount();
			int type1Count=0,type2Count=0,type3Count=0;
			for(int i=1;i<count;i++)
			{
				Row row = table.getRowByIndex(i);
				for(int j=0;j<6;j++)
				{
					Paragraph para = templateSection.getParagraphByIndex(j, false);
					Textbox nameBox = para.getTextboxIterator().next();
					String content = row.getCellByIndex(j).getDisplayText();
					nameBox.setTextContent(content);
					
					if (j==5)
					{
						if (content.equals("Deluxe Room"))
							type1Count++;
						else if (content.equals("Studio/Junior Suite"))
							type2Count++;
						else if (content.equals("Executive Suite"))
							type3Count++;
					}
				}
				doc.appendSection(templateSection, false);
				doc.addParagraph(null);
			}
			templateSection.remove();
			roomtable.getCellByPosition(2,1).setStringValue(type1Count+"");
			roomtable.getCellByPosition(2,2).setStringValue(type2Count+"");
			roomtable.getCellByPosition(2,3).setStringValue(type3Count+"");
			
			doc.getContentRoot().appendChild(roomtable.getOdfElement());
			doc.save("output.odt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

