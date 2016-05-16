package com.opencdk.dynamicaction;

import java.util.List;

/**
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2016-3-13
 * @Modify 2016-3-13
 */
public class DAPackage {

	private String id;
	private String packageName;
	private List<DAAction> actions;

	@Override
	public String toString() {
		return "DAPackage [id=" + id + ", packageName=" + packageName + "]";
	}

	public String getPackageId() {
		return id;
	}

	public void setPackageId(String id) {
		this.id = id;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public List<DAAction> getActions() {
		return actions;
	}

	public void setActions(List<DAAction> actions) {
		this.actions = actions;
	}

}
