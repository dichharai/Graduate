require 'part3.rb'

describe "dessert" do
  it "should define a constructor" do
#    lambda { Dessert.new('a', 1) }.should_not raise_error
    expect(lambda {Dessert.new('a', 1)}).not_to raise_error
  end
 [:healthy?, :delicious?].each do |method|
    it "should define #{method}" do
#      Dessert.new('a',1).should respond_to method
      expect(Dessert.new('a',1)).to respond_to(method)
    end
  end
end

describe "jellybean" do
  it "should define a constructor" do
#    lambda { JellyBean.new('a', 1, 2) }.should_not raise_error
    expect(lambda { JellyBean.new('a', 1, 2) }).not_to raise_error
  end
    [:healthy?, :delicious?].each do |method|
    it "should define #{method}" do
#      JellyBean.new('a',1, 2).should respond_to method 
      expect(JellyBean.new('a',1, 2)).to respond_to(method) 
    end
  end
end
