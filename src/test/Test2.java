//窗口：.win:time_batch
//事件：map
package test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

class AppleListener2 implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			   for (EventBean newEvent : newEvents) {
                    String newEventName = newEvent.getEventType().getName();
                    System.out.println("newEventName:"+newEventName);
                    System.out.println("name:"+newEvent.get("name"));
                    System.out.println("age:"+ newEvent.get("age"));
                    System.out.println("***********");
		}
	}
		  System.out.println("================");
	}
}

public class Test2 {

	public static void main(String[] args) throws InterruptedException {
		EPServiceProvider epService = EPServiceProviderManager
				.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		// Person定义
		Map<String, Object> personTable = new HashMap<String, Object>();
		personTable.put("name", String.class);
		personTable.put("age", Integer.class);

		// 注册Person到Esper
		admin.getConfiguration().addEventType("personEventTypeName",personTable);
		String product = "personEventTypeName";
		// 统计窗口限定为：5秒。每攒够5秒就计算一次，然后清空队列。
		//.std:unique(name)表示
		String epl = "select * from " + product + ".win:time_batch(5 sec).std:unique(name)";

		EPStatement state = admin.createEPL(epl);
		state.addListener(new AppleListener2());
		Map<String, Object> person;
		int i = 0;
		while (true) {
			//必须要创建新的对象，而不能 person.clear();
			person = new HashMap<String, Object>();
			person.put("name", "小明" + i);
			person.put("age", i++);
			runtime.sendEvent(person, "personEventTypeName");
			TimeUnit.SECONDS.sleep(1);
		}
	}
}
/**
 *newEventName:personEventTypeName
name:小明0
age:0
***********
newEventName:personEventTypeName
name:小明1
age:1
***********
newEventName:personEventTypeName
name:小明2
age:2
***********
newEventName:personEventTypeName
name:小明3
age:3
***********
newEventName:personEventTypeName
name:小明4
age:4
***********
================
newEventName:personEventTypeName
name:小明5
age:5
***********
newEventName:personEventTypeName
name:小明6
age:6
***********
newEventName:personEventTypeName
name:小明7
age:7
***********
newEventName:personEventTypeName
name:小明8
age:8
***********
newEventName:personEventTypeName
name:小明9
age:9
***********
================
 */
