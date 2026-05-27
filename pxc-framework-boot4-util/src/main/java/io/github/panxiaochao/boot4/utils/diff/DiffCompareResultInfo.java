package io.github.panxiaochao.boot4.utils.diff;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 比对结果信息
 * </p>
 *
 * @author lypxc
 * @since 2026-01-12
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class DiffCompareResultInfo {

	/**
	 * 行号
	 */
	private int lineNum;

	/**
	 * 比对结果标签，可选值：EQUAL、INSERT、DELETE、CHANGE
	 */
	private String tag;

	/**
	 * 旧行内容
	 */
	private String oldLine;

	/**
	 * 新行内容
	 */
	private String newLine;

	/**
	 * 构造方法：差异比较结果信息
	 * @param lineNum 行号
	 * @param tag 比对结果标签
	 * @param oldLine 旧行内容
	 * @param newLine 新行内容
	 */
	public DiffCompareResultInfo(int lineNum, String tag, String oldLine, String newLine) {
		this.lineNum = lineNum;
		this.tag = tag;
		this.oldLine = oldLine;
		this.newLine = newLine;
	}

	/**
	 * 静态工厂方法：差异比较结果信息
	 * @param lineNum 行号
	 * @param tag 比对结果标签
	 * @param oldLine 旧行内容
	 * @param newLine 新行内容
	 * @return 差异比较结果信息
	 */
	public static DiffCompareResultInfo of(int lineNum, String tag, String oldLine, String newLine) {
		return new DiffCompareResultInfo(lineNum, tag, oldLine, newLine);
	}

}
