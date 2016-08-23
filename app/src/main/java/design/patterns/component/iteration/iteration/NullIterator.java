/*
 * 文 件 名:  NullIterator.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2015年12月29日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package design.patterns.component.iteration.iteration;

import java.util.Iterator;

import design.patterns.component.iteration.superclass.AbsComponent;

/**
 * 空迭代器，表示没有可以支持迭代的元素。
 * <功能详细描述>
 * 
 * @author  sWX284798
 * @version  [版本号, 2015年12月29日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class NullIterator implements Iterator<AbsComponent>
{

    /** {@inheritDoc} */
     
    @Override
    public boolean hasNext()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /** {@inheritDoc} */
     
    @Override
    public AbsComponent next()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
     
    @Override
    public void remove()
    {
        // TODO Auto-generated method stub   
        throw new UnsupportedOperationException();
    }

}
