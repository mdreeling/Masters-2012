require 'spec_helper'

describe "media/new" do
  before(:each) do
    assign(:medium, stub_model(Medium,
      :name => "MyString"
    ).as_new_record)
  end

  it "renders new medium form" do
    render

    # Run the generator again with the --webrat flag if you want to use webrat matchers
    assert_select "form", :action => media_path, :method => "post" do
      assert_select "input#medium_name", :name => "medium[name]"
    end
  end
end
